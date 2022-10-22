import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, take, tap} from 'rxjs';
import {AuthService} from "../auth/auth.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {

    constructor(
        private router: Router,
        private authService: AuthService,
    ) {
    }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        return this.authService.isAuthenticated
                   .pipe(
                       take(1),
                       tap((isAuthenticated: boolean) => {
                           if (!isAuthenticated) {
                               this.router.navigate(['/login']).then(r => console.log)
                           }
                       })
                   );
    }

}
