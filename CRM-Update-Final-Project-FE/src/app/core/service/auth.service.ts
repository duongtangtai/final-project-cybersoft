import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { LocalStorageService } from "ngx-webstorage";
import { BehaviorSubject, map } from "rxjs";
import { User } from "../../model/user.model";

@Injectable({
  providedIn: "root"
})
export class AuthService {

  public _isLogin = new BehaviorSubject<any>(false);
  private _user = new BehaviorSubject<any>([]);

  constructor(
    private localSt: LocalStorageService,
    private http: HttpClient,
    private router: Router
    // @Inject(APP_CONFIG) private config: PTSAppConfig
  ) {
  }

  get isAuth() {
    return this._user.asObservable().pipe(
      map(user => {
        if (user) {
          return !!user.token;
        }
        return false;
      })
    );
  }

  get token() {
    return this._user.asObservable().pipe(
      map((user) => {
        if (user) {
          return user.token;
        }
        return null;
      })
    );
  }

  private setUserData = (userData: AuthResponseData) => {
    const expirationTime = new Date(
      new Date().getTime() + +userData.expiresIn * 1000
    );
    const user = new User(
      userData.id,
      userData.firstName,
      userData.lastName,
      userData.username,
      userData.email,
      userData.roles,
      userData.token,
      expirationTime
    );
    this._user.next(user);
  };

  // login = (username: string) => {
  //     return this.http
  //                .post<AuthResponseData>(`${this.config.endpoints.auth.login}`, username)
  //                .pipe(
  //                    tap(this.setUserData)
  //                );
  // };
}

export interface AuthResponseData {
  id: number;
  token: string;
  email: string;
  username: string;
  roles: {
    id: number;
    roleName: string;
  }[];
  expiresIn: string;
  firstName: string;
  lastName: string;
}