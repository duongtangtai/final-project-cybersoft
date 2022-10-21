import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {AppSettings} from "../../app.constants";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const authData = window.localStorage.getItem(AppSettings.AUTH_DATA);
        let data: any;
        if (authData != null) {
            data = JSON.parse(authData) as {
                userId: string;
                firstName: string;
                lastName: string;
                username: string;
                email: string;
                roles: {
                    id: number;
                    roleName: string;
                }[];
                dept: string;
                token: string;
                tokenExpirationDate: Date;
            };
            const modifiedReq = request.clone({
                headers: request.headers.set('Authorization', `Bearer ${data.token}`),
            });
            return next.handle(modifiedReq);
        }
        return next.handle(request);
    }
}
