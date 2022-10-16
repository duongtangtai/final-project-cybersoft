import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { LocalStorageService } from "ngx-webstorage";
import { Observable } from "rxjs";
import { AppConstraint } from "../../app.constaint";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private localSt: LocalStorageService
  ) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return !!this.localSt.retrieve(AppConstraint.IS_LOGIN);
  }
}
