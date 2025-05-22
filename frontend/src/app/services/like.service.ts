import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LikeDTO } from '../models/like.model';

@Injectable({
  providedIn: 'root',
})
export class LikeService {
  private baseUrl = 'http://localhost:8080/like';

  constructor(private http: HttpClient) {}

  /**
   * Send a like or dislike.
   * POST /like/addlike → 201 + LikeDTO
   */
  addLike(dto: LikeDTO): Observable<LikeDTO> {
    return this.http.post<LikeDTO>(`${this.baseUrl}/addlike`, dto);
  }
}
