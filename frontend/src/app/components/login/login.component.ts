import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;
  errorMessage: string | null = null;

  constructor(private userService: UserService, private router: Router) {}

  onSubmit(): void {
    this.isLoading = true;
    this.errorMessage = null;

    this.userService.login(this.email, this.password).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/swipe']);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = 'Invalid email or password. Please try again.';
        console.error('Login error:', error);
      },
    });
  }
}
