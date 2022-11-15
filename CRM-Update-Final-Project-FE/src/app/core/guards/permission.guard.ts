import { AppSettings } from 'src/app/app.constants';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, filter } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PermissionGuard implements CanActivate {
  constructor(
    private router: Router
  ) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const requiredRoles = route.data['requiredRoles'];
    for (let i = 0 ; i < requiredRoles.length ; i ++) {
      if (AppSettings.USER_ROLES.includes(requiredRoles[i])) {
        return true;
      }
    }
    this.router.navigateByUrl(AppSettings.PATH_403)
    return false;
  }
}
