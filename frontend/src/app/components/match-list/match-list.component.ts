import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { MatchService } from '../../services/match.service';
import { MatchDTO } from '../../models/match.model';
import { UserDTO } from '../../models/user.model';

interface MatchWithUser {
  match: MatchDTO;
  user: UserDTO;
  lastMessage?: string;
  lastMessageTime?: string;
}

@Component({
  selector: 'app-match-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.css'],
})
export class MatchListComponent implements OnInit {
  allMatches: MatchWithUser[] = [];
  filteredMatches: MatchWithUser[] = [];
  searchTerm: string = '';
  isLoading: boolean = false;
  isDeletingMatch: boolean = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private userService: UserService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    this.loadMatches();
  }

  get currentUser(): UserDTO | null {
    return this.userService.currentUserValue;
  }

  loadMatches(): void {
    if (!this.currentUser?.id) {
      this.errorMessage = 'You must be logged in to view matches';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    this.matchService.findMatches(this.currentUser.id).subscribe({
      next: (matches) => {
        this.isLoading = false;
        // For each match, we need to fetch the other user's details
        this.loadMatchedUsers(matches);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load matches. Please try again.';
        console.error('Matches loading error:', error);
      },
    });
  }

  private loadMatchedUsers(matches: MatchDTO[]): void {
    if (!this.currentUser?.id || matches.length === 0) {
      this.allMatches = [];
      this.filteredMatches = [];
      return;
    }

    // Reset matches arrays
    this.allMatches = [];
    this.filteredMatches = [];

    // Keep track of users we've already added to prevent duplicates
    const processedUserIds = new Set<number>();

    // For each match, determine the other user ID and fetch their details
    matches.forEach((match) => {
      // Ensure this is a valid match (has both user IDs)
      if (!match.user1Id || !match.user2Id) {
        return;
      }

      const otherUserId =
        match.user1Id === this.currentUser?.id ? match.user2Id : match.user1Id;

      // Skip if we've already processed this user
      if (processedUserIds.has(otherUserId)) {
        return;
      }

      // Mark this user as processed
      processedUserIds.add(otherUserId);

      this.userService.getUserById(otherUserId).subscribe({
        next: (user) => {
          const matchWithUser: MatchWithUser = {
            match: match,
            user: user,
            // In a real app, you would fetch the last message from a message service
            lastMessage: 'Tap to start chatting',
            lastMessageTime: 'New match',
          };

          this.allMatches.push(matchWithUser);
          this.applyFilter(); // Apply any existing filter
        },
        error: (error) => {
          console.error(`Failed to load user ${otherUserId}:`, error);
        },
      });
    });
  }

  applyFilter(): void {
    if (!this.searchTerm.trim()) {
      this.filteredMatches = [...this.allMatches];
      return;
    }

    const searchTermLower = this.searchTerm.toLowerCase().trim();
    this.filteredMatches = this.allMatches.filter((match) =>
      match.user.profileName.toLowerCase().includes(searchTermLower)
    );
  }

  onSearchChange(): void {
    this.applyFilter();
  }

  unmatch(matchId: number | undefined): void {
    if (!matchId) return;

    this.isDeletingMatch = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.matchService.deleteMatch(matchId).subscribe({
      next: () => {
        this.isDeletingMatch = false;
        this.successMessage = 'Unmatched successfully';

        // Remove the match from both arrays
        this.allMatches = this.allMatches.filter((m) => m.match.id !== matchId);
        this.filteredMatches = this.filteredMatches.filter(
          (m) => m.match.id !== matchId
        );
      },
      error: (error) => {
        this.isDeletingMatch = false;
        this.errorMessage = 'Failed to unmatch. Please try again.';
        console.error('Unmatch error:', error);
      },
    });
  }
}
