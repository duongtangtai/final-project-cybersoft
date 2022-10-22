export interface IUserModel {
    id?: string,
    username?: string,
    email?: string,
    firstName?: string,
    lastName?: string,
    roleCodes?: string[],
    accessToken?: string,
    refreshToken?: string
}

export class UserModel implements IUserModel {
    constructor(
        public id?: string,
        public username?: string,
        public email?: string,
        public firstName?: string,
        public lastName?: string,
        public roleCodes?: string[],
        public accessToken?: string,
        public refreshToken?: string
    ) {
    }
}