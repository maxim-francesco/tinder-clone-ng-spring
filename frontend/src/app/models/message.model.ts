export interface MessageDTO {
  id?: number;
  userSenderId: number;
  userReceiverId: number;
  matchId: number;
  content: string;
  dateTime?: string; // ISO timestamp
}
