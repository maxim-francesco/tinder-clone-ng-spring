import { Routes } from '@angular/router';
import { SwipeComponent } from './components/swipe/swipe.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { MatchListComponent } from './components/match-list/match-list.component';
import { ChatComponent } from './components/chat/chat.component';
import { ProfileComponent } from './components/profile/profile.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'swipe', component: SwipeComponent },
  { path: 'users', component: UserListComponent },
  { path: 'matches', component: MatchListComponent },
  { path: 'chat/:matchId', component: ChatComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', redirectTo: 'login' },
];
