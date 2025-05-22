import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MessageDTO } from '../models/message.model';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private baseUrl = 'http://localhost:8080/messages';

  constructor(private http: HttpClient) {}

  /**
   * Send a new message via POST /messages/send
   */
  sendMessage(dto: MessageDTO): Observable<MessageDTO> {
    return this.http.post<MessageDTO>(`${this.baseUrl}/send`, dto);
  }

  /**
   * Get all messages for a given match via GET /messages/allbymatch/{matchId}
   */
  getAllByMatch(matchId: number): Observable<MessageDTO[]> {
    return this.http.get<MessageDTO[]>(`${this.baseUrl}/allbymatch/${matchId}`);
  }
}
