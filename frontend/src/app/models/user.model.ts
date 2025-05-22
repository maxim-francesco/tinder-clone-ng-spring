export interface UserDTO {
  id?: number;
  email: string;
  password: string;
  // flattened profile fields from the backend UserDTO
  profileName: string;
  profileAge: number;
  profileGender: string;
  profileBio: string;
  profileLocation: string;
}
