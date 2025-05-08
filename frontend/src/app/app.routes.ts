import { Routes } from '@angular/router';
import { MainComponent } from './components/auth/main/main.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { LoginComponent } from './components/auth/login/login.component';
import { FailedComponent } from './components/pages/failed/failed.component';
import { MainPageComponent } from './components/pages/main-page/main-page.component';

export const routes: Routes = [
  {
    path: '',
    component: MainComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'failed',
    component: FailedComponent,
  },
  {
    path: 'mainpage',
    component: MainPageComponent,
  },
  {
    path: '**',
    component: MainComponent,
  },
];
