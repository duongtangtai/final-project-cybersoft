export interface IUserModel {
    //USER
    //accessToken
    //refreshToken
    userData: {
        id?: string,
        username?: string,
        firstName?: string,
        lastName?: string,
        gender?: string,
        avatar?: string,
        email?: string,
        facebookUrl?: string,
        occupation?: string,
        department?: string,
        hobbies?: string,
        accountStatus?: string,
    },
    roleCodes?: string[],
    accessToken?: string,
    refreshToken?: string
}

export class UserModel implements IUserModel {
    constructor(
        public userData: {
            id?: string,
            username?: string,
            firstName?: string,
            lastName?: string,
            gender?: string,
            avatar?: string,
            email?: string, 
            facebookUrl?: string,
            occupation?: string,
            department?: string,
            hobbies?: string,
            accountStatus?: string,
        },
        public roleCodes?: string[],
        public accessToken?: string,
        public refreshToken?: string
    ) {
    }
    // constructor(
    //     public id?: string,
    //     public username?: string,
    //     public email?: string,
    //     public firstName?: string,
    //     public lastName?: string,
    //     public avatar?: string,
    //     public roleCodes?: string[],
    //     public accessToken?: string,
    //     public refreshToken?: string
    // ) {
    // }
}