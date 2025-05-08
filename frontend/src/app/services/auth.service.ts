import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Router } from '@angular/router';
import { error } from 'console';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private url = 'http://localhost:8080/user';

  login(email: string, password: string) {
    this.http.get<User>(this.url + `/findbyemail/${email}`).subscribe(
      (response) => {
        if (response == undefined) return;
        else {
          if (response.password != password) {
            this.router.navigate(['/failed']);
          } else {
            this.router.navigate(['/mainpage']);
          }
        }
      },
      (error) => {
        this.router.navigate(['/failed']);
      }
    );
  }

  register(user: User) {
    this.http.post<User>(this.url + '/add', user).subscribe(
      (respone) => {
        this.router.navigate(['/mainpage']);
      },
      (error) => {
        this.router.navigate(['/failed']);
      }
    );
  }

  getAll() {
    this.http.get<User[]>(this.url + '/all').subscribe((response) => {
      console.log(response);
    });
  }
}
