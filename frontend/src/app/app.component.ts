import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { MainComponent } from './components/auth/main/main.component';
import { RegisterComponent } from './components/auth/register/register.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoginComponent, MainComponent, RegisterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';
}
