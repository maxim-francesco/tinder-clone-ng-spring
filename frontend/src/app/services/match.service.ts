import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MatchDTO } from '../models/match.model';

@Injectable({
  providedIn: 'root',
})
export class MatchService {
  private baseUrl = 'http://localhost:8080/match';

  constructor(private http: HttpClient) {}

  /**
   * Create a new match given two user IDs.
   * POST /match/add → 201 + MatchDTO
   */
  addMatch(dto: MatchDTO): Observable<MatchDTO> {
    return this.http.post<MatchDTO>(`${this.baseUrl}/add`, dto);
  }

  /**
   * Get all matches for a user.
   * GET /match/findbyid/{userId} → 200 + MatchDTO[]
   */
  findMatches(userId: number): Observable<MatchDTO[]> {
    return this.http.get<MatchDTO[]>(`${this.baseUrl}/findbyid/${userId}`);
  }

  /**
   * Delete a match by its ID.
   * DELETE /match/deletebyid/{matchId} → 204 No Content
   */
  deleteMatch(matchId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deletebyid/${matchId}`);
  }
}
