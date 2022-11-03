import { AppSettings } from './../../app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthService } from './../auth/auth.service';
import { MyToastrService } from './../../share/services/my-toastr.service';
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest,HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, map, switchMap, take } from 'rxjs/operators';



@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  private isRefreshing: boolean = false;
  private newAccessTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null)
  
    constructor(
      private myToastrService: MyToastrService,
      private authService: AuthService,
      private localStorageService: LocalStorageService,
    ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
        .pipe(catchError((exception: HttpErrorResponse) => {
            if (exception.status == 400) {
                this.myToastrService.error(exception.error.errors)
            } else if (exception.status == 401) {
              return this.handle401Request(request, next)
            }
            return throwError("");
         }))
  }

  private handle401Request(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {  //if no request is trying to refresh token
      this.isRefreshing = true; // other requests must wait for this request to finish
      this.newAccessTokenSubject.next(null)
      const refreshToken = this.localStorageService.retrieve(AppSettings.AUTH_DATA).refreshToken;
      this.authService.refreshToken(refreshToken).subscribe(content =>{
        const newAccessToken = content.accessToken;
        const newRefreshToken = content.refreshToken;
        this.storeNewTokens(newAccessToken, newRefreshToken);
        this.newAccessTokenSubject.next(newAccessToken) // send delayed requests
      })
    } //else wait for new access token
    return this.newAccessTokenSubject.pipe(
        filter(newAccessToken => newAccessToken!=null),
        take(1),
        switchMap((token) => next.handle(this.requestWithNewAccessToken(request, token)))
    )
  }

  private storeNewTokens(newAccessToken: string, newRefreshToken: string) {
    const oldData = this.localStorageService.retrieve(AppSettings.AUTH_DATA);
    const newData = {
      accessToken : newAccessToken,
      refreshToken : newRefreshToken,
      userData : oldData.userData,
      roleCodes : oldData.roleCodes,
    }
    this.localStorageService.store(AppSettings.AUTH_DATA, newData)
  }

  private requestWithNewAccessToken(request: HttpRequest<any>, newAccessToken: string) {
    return request.clone({
      headers : request.headers.set('Authorization', `Bearer ${newAccessToken}`)
    })
  }
}