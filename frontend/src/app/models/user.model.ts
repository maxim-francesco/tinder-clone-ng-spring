export interface User {
  id?: string;
  email?: string;
  birth?: string;
  name?: string;
  gender?: string;
  sexualOrientation: { [key: string]: boolean };
  password?: string;
}
