import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user: UserDTO = {
    email: '',
    password: '',
    profileName: '',
    profileAge: 18,
    profileGender: '',
    profileBio: '',
    profileLocation: '',
  };

  confirmPassword: string = '';
  isLoading: boolean = false;
  errorMessage: string | null = null;

  constructor(private userService: UserService, private router: Router) {}

  onSubmit(): void {
    // Double-check matching passwords before sending to backend
    if (this.user.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    this.userService.register(this.user).subscribe({
      next: () => {
        this.isLoading = false;
        // Navigate to “swipe” (home) after successful registration
        this.router.navigate(['/swipe']);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage =
          error.message || 'Registration failed. Please try again.';
        console.error('Registration error:', error);
      },
    });
  }
}
