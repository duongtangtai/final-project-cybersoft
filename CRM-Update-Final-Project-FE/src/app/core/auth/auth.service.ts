import { ProfileService } from './../../pages/services/profile.service';
import {HttpBackend, HttpClient} from "@angular/common/http";
import {Inject, Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {BehaviorSubject, from, map, Observable, tap} from "rxjs";
import {AppSettings} from "../../app.constants";
import {IUserModel, UserModel} from "../../model/user.model";
import {APP_CONFIG, PTSAppConfig} from "../config/app.config";
import {IRequestModel} from "../request/request.model";

@Injectable({
    providedIn: "root"
})
export class AuthService {

    private _user = new BehaviorSubject<any>(this.localStorageService.retrieve(AppSettings.AUTH_DATA));
    private httpBackend: HttpClient;

    constructor(
        private router: Router,
        private http: HttpClient,
        private localStorageService: LocalStorageService,
        @Inject(APP_CONFIG) private config: PTSAppConfig,
        private handler: HttpBackend,
        private profileService: ProfileService,
    ) {
        this.httpBackend = new HttpClient(handler)
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

    get userId() {
        return this._user.asObservable().pipe(
            map((user: UserModel) => {
                if (user) {
                    return user.userData.id;
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
                   .pipe(
                       map((val: IRequestModel) => val.content),
                       tap(this.setUserData.bind(this)))
    }

    logout() {
        this._user.next(null);
        this.localStorageService.clear();
    }

    autoLogin() {}

    private setUserData(user: IUserModel) {
        this._user.next(new UserModel(
            user.userData,
            user.roleCodes,
            user.accessToken,
            user.refreshToken
        ));
        this.storeAuthData(user);
        const avatarUrl = user.userData.avatar;
        if (avatarUrl != undefined) {
            console.log(avatarUrl)
            this.profileService.sendAvatarUrl(avatarUrl);
        } else {
            this.profileService.sendAvatarUrl(''); //set null to handle with genders
        }
        const userFirstName = user.userData.firstName;
        if (userFirstName != undefined) {
            this.profileService.sendFirstName(userFirstName)
        }
        const userLastName = user.userData.lastName;
        if (userLastName != undefined) {
            this.profileService.sendLastName(userLastName)
        }
        const userRoles = user.roleCodes
        if (userRoles != undefined) {
            this.profileService.sendRoles(userRoles)
        }
    }

    private storeAuthData(userData: IUserModel) {
        this.localStorageService.store(AppSettings.AUTH_DATA, userData);
    }

    refreshToken(refreshToken: string): Observable<any> {
        return this.httpBackend.post<IRequestModel>(`${this.config.endpoints.auth.refreshToken}`, '', {
            params : {
                refreshToken : refreshToken
            }
        }).pipe(map((val: IRequestModel) => val.content))
    }
}
