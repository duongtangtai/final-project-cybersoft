export interface IUser {
  id?: number,
  firstName?: string,
  lastName?: string,
  username?: string,
  email?: string,
  roles?: IRole[],
  token?: string,
  expirationTime?: Date
}

export interface IRole {
  id?: number,
  name?: string
}

export class User implements IUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public username?: string,
    public email?: string,
    public roles?: IRole[],
    public token?: string,
    public expirationTime?: Date
  ) {
  }
}