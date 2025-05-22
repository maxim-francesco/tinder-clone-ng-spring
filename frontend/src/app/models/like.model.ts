export interface LikeDTO {
  id?: number;
  userFromId: number;
  userToId: number;
  liked: boolean;
  createdAt?: string; // ISO timestamp
}
