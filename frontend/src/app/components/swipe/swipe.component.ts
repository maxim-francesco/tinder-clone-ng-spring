import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { LikeService } from '../../services/like.service';
import { MatchService } from '../../services/match.service';
import { UserDTO } from '../../models/user.model';
import { LikeDTO } from '../../models/like.model';
import { MatchDTO } from '../../models/match.model';
import { forkJoin, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Component({
  selector: 'app-swipe',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './swipe.component.html',
  styleUrls: ['./swipe.component.css'],
})
export class SwipeComponent implements OnInit {
  users: UserDTO[] = [];
  currentIndex: number = 0;
  isLoading: boolean = false;
  isActionLoading: boolean = false;
  errorMessage: string | null = null;
  noMoreProfiles: boolean = false;
  matchedUserIds: Set<number> = new Set();

  constructor(
    private userService: UserService,
    private likeService: LikeService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    this.loadProfiles();
  }

  get currentUser(): UserDTO | null {
    return this.userService.currentUserValue;
  }

  get currentProfile(): UserDTO | null {
    if (this.users.length === 0 || this.currentIndex >= this.users.length) {
      return null;
    }
    return this.users[this.currentIndex];
  }

  loadProfiles(): void {
    if (!this.currentUser?.id) {
      this.errorMessage = 'You must be logged in to view profiles';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    // First, get all matches to filter out users who are already matched
    this.matchService
      .findMatches(this.currentUser.id)
      .pipe(
        catchError((error) => {
          console.error('Error fetching matches:', error);
          return of([] as MatchDTO[]); // Return empty array on error
        })
      )
      .subscribe((matches) => {
        // Extract the IDs of all matched users
        this.matchedUserIds.clear();
        matches.forEach((match) => {
          const otherUserId =
            match.user1Id === this.currentUser?.id
              ? match.user2Id
              : match.user1Id;
          this.matchedUserIds.add(otherUserId);
        });

        console.log('Matched user IDs:', Array.from(this.matchedUserIds));

        // Now fetch all users and filter out the current user and matched users
        this.userService.getAllUsers().subscribe({
          next: (users) => {
            // Filter out:
            // 1. The current user
            // 2. Users who are already matched with the current user
            this.users = users.filter(
              (user) =>
                user.id !== this.currentUser?.id &&
                !this.matchedUserIds.has(user.id!)
            );

            console.log(
              `Filtered from ${users.length} to ${this.users.length} users`
            );

            this.isLoading = false;
            this.currentIndex = 0;
            this.noMoreProfiles = this.users.length === 0;
          },
          error: (error) => {
            this.isLoading = false;
            this.errorMessage = 'Failed to load profiles. Please try again.';
            console.error('Profile loading error:', error);
          },
        });
      });
  }

  onLike(): void {
    this.handleSwipe(true);
  }

  onDislike(): void {
    this.handleSwipe(false);
  }

  private handleSwipe(liked: boolean): void {
    if (
      !this.currentUser?.id ||
      !this.currentProfile?.id ||
      this.isActionLoading
    ) {
      return;
    }

    this.isActionLoading = true;
    this.errorMessage = null;

    const likeDto: LikeDTO = {
      userFromId: this.currentUser.id,
      userToId: this.currentProfile.id,
      liked: liked,
    };

    console.log('Sending like data:', JSON.stringify(likeDto));

    this.likeService.addLike(likeDto).subscribe({
      next: (response) => {
        console.log('Like response:', response);
        this.isActionLoading = false;

        // If this was a like, check if it created a new match
        if (liked) {
          this.checkForNewMatch();
        } else {
          this.moveToNextProfile();
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error('Like error details:', error);
        this.handleLikeError(error);
      },
    });
  }

  private checkForNewMatch(): void {
    // In a real app, you would have an endpoint to check if a match was created
    // For now, we'll just move to the next profile and refresh matches on next load
    this.moveToNextProfile();

    // Show a message indicating a potential match
    this.errorMessage =
      'Your like was recorded! Check your matches to see if you matched.';
  }

  private handleLikeError(error: any): void {
    this.isActionLoading = false;

    // Move to next profile anyway to prevent getting stuck
    this.moveToNextProfile();

    // Show a more user-friendly error message
    this.errorMessage =
      "We couldn't record your choice, but we've moved to the next profile.";

    // Log detailed error for debugging
    if (error instanceof HttpErrorResponse) {
      console.error('HTTP Error:', {
        status: error.status,
        statusText: error.statusText,
        url: error.url,
        message: error.message,
        error: error.error,
      });
    } else {
      console.error('Non-HTTP Error:', error);
    }
  }

  private moveToNextProfile(): void {
    this.currentIndex++;
    if (this.currentIndex >= this.users.length) {
      this.noMoreProfiles = true;
    }
  }

  refreshProfiles(): void {
    this.loadProfiles();
  }

  skipProfile(): void {
    this.moveToNextProfile();
  }
}
