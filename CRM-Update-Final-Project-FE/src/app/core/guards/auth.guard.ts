import { AppSettings } from 'src/app/app.constants';
import {Injectable} from '@angular/core';
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot,
    RoutesRecognized,
    UrlTree
} from '@angular/router';
import {filter, Observable, pairwise, take, tap} from 'rxjs';
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
                               this.router.navigateByUrl(AppSettings.PATH_LOGIN).then(r => console.log)
                           }
                       })
                   );
    }

}
