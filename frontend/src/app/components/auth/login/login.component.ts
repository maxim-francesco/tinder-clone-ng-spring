import { Component, inject } from '@angular/core';
import { ButtonComponent } from '../../shared/button/button.component';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [ButtonComponent, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private router = inject(Router);
  private authService = inject(AuthService);

  showMain: boolean = true;
  showEmail: boolean = false;
  showPassword: boolean = false;

  email: string = '';
  password: string = '';

  toggleMain() {
    this.router.navigate(['/main']);
  }

  toggleEmail() {
    this.showMain = !this.showMain;
    this.showEmail = !this.showEmail;
  }

  togglePassword() {
    this.showEmail = !this.showEmail;
    this.showPassword = !this.showPassword;
  }

  toggleMainPage() {
    this.authService.login(this.email, this.password);
  }
}
