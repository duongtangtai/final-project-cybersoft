import { HttpErrorInterceptor } from './../../core/interceptor/error-handle.interceptor';
import { BehaviorSubject } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { AppSettings } from 'src/app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private localStorageService: LocalStorageService,
  ) {
  }


  subscribeNotification() {
    var subscribeEndpoint = this.getSubscribeEndPoint()
    var accessToken = this.getAccessToken()
    var eventSrc = new EventSource(subscribeEndpoint + accessToken);
    eventSrc.onerror = () => {
      setTimeout(() => {
        eventSrc = this.subscribeNotification()
      }, 3000)
    }
    return eventSrc;
  }

  getSubscribeEndPoint() {
    return this.config.endpoints.notification.subscribe
  }

  getAccessToken() {
    return this.localStorageService.retrieve(AppSettings.AUTH_DATA).accessToken
  }

  getNewNotification(userId: string) {
    return this.http.get<IRequestModel>(`${this.config.endpoints.notification.getNewByReceiver}` + userId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getOldNotification(userId: string) {
    return this.http.get<IRequestModel>(`${this.config.endpoints.notification.getOldByReceiver}` + userId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  deleteById(notificationId: string) {
    return this.http.delete<IRequestModel>(`${this.config.endpoints.notification.root}` + notificationId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  readAllByReceiver(userId: string) {
    return this.http.post<IRequestModel>(`${this.config.endpoints.notification.readAllByReceiver}` + userId, '')
      .pipe(map((val: IRequestModel) => val.content));
  }


  //seen notification => call BE to disable 

  //subcribe to receive notification from BE

}
