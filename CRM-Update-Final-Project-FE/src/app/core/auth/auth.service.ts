import {HttpClient} from "@angular/common/http";
import {Inject, Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {BehaviorSubject, map, Observable, tap} from "rxjs";
import {IUserModel, UserModel} from "../../model/user.model";
import {APP_CONFIG, PTSAppConfig} from "../config/app.config";
import {IRequestModel} from "../request/request.model";

@Injectable({
    providedIn: "root"
})
export class AuthService {

    private _user = new BehaviorSubject<UserModel>({});

    constructor(
        private storageService: LocalStorageService,
        private http: HttpClient,
        private router: Router,
        @Inject(APP_CONFIG) private config: PTSAppConfig
    ) {
    }

    get isAuthenticated() {
        return this._user.asObservable().pipe(
            map((user: UserModel) => {
                if (user) {
                    return !!user.accessToken;
                }
                return false;
            })
        );
    }

    get token() {
        return this._user.asObservable().pipe(
            map((user: UserModel) => {
                if (user) {
                    return user.accessToken;
                }
                return null;
            })
        );
    }

    login(username: string, password: string): Observable<UserModel> {
        const body = {
            username, password
        };
        return this.http
                   .post<IRequestModel>(`${this.config.endpoints.auth.login}`, body)
                   .pipe(map((val: IRequestModel) => val.content))
                   .pipe(
                       tap(this.setUserData)
                   );
    };

    private setUserData(userData: IUserModel) {
        console.log(userData)
        const userModel = new UserModel(
            userData.username,
            userData.email,
            userData.firstName,
            userData.lastName,
            userData.roleCodes,
            userData.accessToken,
            userData.refreshToken
        );
        this._user.next(userModel);
    };
}
