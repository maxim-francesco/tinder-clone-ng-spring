import { Component, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../shared/button/button.component';
import { NgClass, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ButtonComponent, NgClass, FormsModule, NgFor],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  orientations: string[] = ['Straight', 'Lesbian', 'Bisexual', 'Other'];
  selectedGender = signal<string | null>(null);
  selectedOrientations: { [key: string]: boolean } = {};

  password: string = '';
  email: string = '';
  name: string = '';
  birth1: string = '';
  birth2: string = '';
  birth3: string = '';

  showPassword: boolean = false;
  showPhone: boolean = false;
  showMain: boolean = true;
  showName: boolean = false;
  showBirth: boolean = false;
  showGender: boolean = false;
  showSexualOrientation: boolean = false;

  selectGender(gender: string) {
    this.selectedGender.set(gender);
  }

  hasSelection(): boolean {
    return Object.values(this.selectedOrientations).some(
      (selected) => selected
    );
  }

  togglePhone() {
    this.showMain = !this.showMain;
    this.showPhone = !this.showPhone;
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
    this.showPhone = !this.showPhone;
  }

  toggleName() {
    this.showPassword = !this.showPassword;
    this.showName = !this.showName;
  }

  toggleBirth() {
    this.showName = !this.showName;
    this.showBirth = !this.showBirth;
  }

  toggleGender() {
    this.showBirth = !this.showBirth;
    this.showSexualOrientation = !this.showSexualOrientation;
  }

  toggleMain() {
    this.router.navigate(['/main']);
  }

  seeInputs() {
    const newUser: User = {
      email: this.email,
      password: this.password,
      name: this.name,
      birth: this.birth1 + '/' + this.birth2 + '/' + this.birth3,
      gender: this.selectedGender()!,
      sexualOrientation: this.selectedOrientations,
    };
    this.authService.register(newUser);
  }

  test() {
    this.authService.getAll();
  }
}
