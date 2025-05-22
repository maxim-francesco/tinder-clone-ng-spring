// src/app/services/user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { UserDTO } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl = 'http://localhost:8080/user';

  /** holds the current logged-in user (or null) purely in memory */
  private currentUserSubject = new BehaviorSubject<UserDTO | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  /** synchronous snapshot of current user */
  get currentUserValue(): UserDTO | null {
    return this.currentUserSubject.value;
  }

  /** update current user in memory */
  private setCurrentUser(user: UserDTO): void {
    this.currentUserSubject.next(user);
  }

  /** clear current user */
  logout(): void {
    this.currentUserSubject.next(null);
  }

  /** attempt login by fetching via email + checking password */
  login(email: string, password: string): Observable<UserDTO> {
    return this.getUserByEmail(email).pipe(
      tap((user) => {
        if (user.password !== password) {
          throw new Error('Invalid credentials');
        }
        this.setCurrentUser(user);
      })
    );
  }

  /** wrap register endpoint */
  register(user: UserDTO): Observable<UserDTO> {
    return this.http
      .post<UserDTO>(`${this.baseUrl}/add`, user)
      .pipe(tap((created) => this.setCurrentUser(created)));
  }

  /** fetch all users */
  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.baseUrl}/all`);
  }

  /** fetch by id */
  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.baseUrl}/findbyid/${id}`);
  }

  /** fetch by email */
  getUserByEmail(email: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.baseUrl}/findbyemail/${email}`);
  }

  /** update profile */
  updateUser(id: number, user: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.baseUrl}/update/${id}`, user).pipe(
      tap((updated) => {
        // if the currently logged-in user updated themself, refresh
        if (this.currentUserValue?.id === updated.id) {
          this.setCurrentUser(updated);
        }
      })
    );
  }
}
