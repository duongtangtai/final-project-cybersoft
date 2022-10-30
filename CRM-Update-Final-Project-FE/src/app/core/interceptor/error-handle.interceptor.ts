import { MyToastrService } from './../../share/services/my-toastr.service';
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse,
         HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';



@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
    constructor(private myToastrService: MyToastrService) {
    }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
        .pipe(catchError((exception: HttpErrorResponse) => {
            if (exception.status == 400) {
                console.log(exception.error.errors)
                this.myToastrService.error(exception.error.errors)
            }
            return throwError("");
         }))
  }
}