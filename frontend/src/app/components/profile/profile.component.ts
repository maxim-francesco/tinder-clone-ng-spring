import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../models/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  user: UserDTO | null = null;
  isEditing: boolean = false;
  isLoading: boolean = false;
  isSaving: boolean = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  // Available interests (this could be fetched from an API in a real app)
  availableInterests: string[] = [
    'Hiking',
    'Photography',
    'Music',
    'Coding',
    'Food',
    'Travel',
    'Reading',
    'Movies',
    'Fitness',
    'Art',
    'Dancing',
    'Cooking',
    'Gaming',
    'Yoga',
    'Sports',
  ];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const currentUser = this.userService.currentUserValue;
    if (!currentUser || !currentUser.id) {
      this.errorMessage = 'You must be logged in to view your profile';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    this.userService.getUserById(currentUser.id).subscribe({
      next: (user) => {
        this.user = user;
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load profile. Please try again.';
        console.error('Profile loading error:', error);
      },
    });
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    this.errorMessage = null;
    this.successMessage = null;
  }

  saveProfile(): void {
    if (!this.user || !this.user.id) return;

    this.isSaving = true;
    this.errorMessage = null;
    this.successMessage = null;

    this.userService.updateUser(this.user.id, this.user).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.isSaving = false;
        this.isEditing = false;
        this.successMessage = 'Profile updated successfully!';
      },
      error: (error) => {
        this.isSaving = false;
        this.errorMessage = 'Failed to update profile. Please try again.';
        console.error('Profile update error:', error);
      },
    });
  }

  // Note: In a real app, you would implement methods to handle interests
  // as they're not part of the UserDTO model. This would require additional
  // API endpoints and services.
}
