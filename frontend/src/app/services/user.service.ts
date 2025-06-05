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

  /** reține în memorie utilizatorul curent autentificat */
  private currentUserSubject = new BehaviorSubject<UserDTO | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  /** instantaneu: cine este utilizatorul logat */
  get currentUserValue(): UserDTO | null {
    return this.currentUserSubject.value;
  }

  /** actualizează utilizatorul logat în memorie */
  private setCurrentUser(user: UserDTO): void {
    this.currentUserSubject.next(user);
  }

  /** dezautentifică utilizatorul */
  logout(): void {
    this.currentUserSubject.next(null);
  }

  /**
   * POST /user/login
   * Trimite { email, password } la backend;
   * dacă răspunde 200, stochează User-ul returnat în BehaviorSubject.
   */
  login(email: string, password: string): Observable<UserDTO> {
    return this.http
      .post<UserDTO>(`${this.baseUrl}/login`, { email, password })
      .pipe(
        tap((user) => {
          // dacă login-ul e valid, setăm user-ul curent
          this.setCurrentUser(user);
        })
      );
  }

  /**
   * POST /user/signup
   * Trimite un UserDTO (conținând email + password + câmpuri din profil) la backend.
   * Dacă răspunde 201, stochează User-ul returnat în BehaviorSubject.
   */
  register(user: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.baseUrl}/signup`, user).pipe(
      tap((created) => {
        // după înregistrare, considerăm user-ul autentificat
        this.setCurrentUser(created);
      })
    );
  }

  /** GET /user/all */
  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(`${this.baseUrl}/all`);
  }

  /** GET /user/findbyid/{id} */
  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.baseUrl}/findbyid/${id}`);
  }

  /** GET /user/findbyemail/{email} */
  getUserByEmail(email: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.baseUrl}/findbyemail/${email}`);
  }

  /**
   * PUT /user/update/{id}
   * Actualizează datele de profil ale unui user.
   * Dacă id-ul corespunde utilizatorului logat, actualizează și BehaviorSubject-ul.
   */
  updateUser(id: number, user: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.baseUrl}/update/${id}`, user).pipe(
      tap((updated) => {
        if (this.currentUserValue?.id === updated.id) {
          this.setCurrentUser(updated);
        }
      })
    );
  }
}
