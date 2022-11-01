import { AppSettings } from './../../app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import {BehaviorSubject, map, Observable, pipe} from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';
import { IStaffModel } from 'src/app/model/staff.model';
import { MyToastrService } from 'src/app/share/services/my-toastr.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private avatarLink: BehaviorSubject<string> = new BehaviorSubject<string>('');
  data: Observable<string> = this.avatarLink.asObservable();
  private UPDATE_PROFILE = 'update-profile';
  private CHANGE_PASSWORD = 'change-password';

  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService,
    private localStorageService: LocalStorageService,
  ) {}

  updateProfile(profile: IStaffModel): Observable<IStaffModel>{
    return this.http.put<IRequestModel>(`${this.config.endpoints.profile.root}` + this.UPDATE_PROFILE, profile)
      .pipe(map((val: IRequestModel) => val.content));
  }

  uploadAvatar(submitForm: any): Observable<any> {
    return this.http.post(`${this.config.endpoints.file.root}`, submitForm)
      .pipe(map((val: any) => val.content));
  }

  sendData(data: string) {
    this.avatarLink.next(data);
  }

  changePassword(submitForm: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    }
    console.log(submitForm)
    console.log(this.config.endpoints.profile.root + this.CHANGE_PASSWORD)
    return this.http.put(`${this.config.endpoints.profile.root}` + this.CHANGE_PASSWORD, submitForm)
      .pipe(map((val: any) => val.content));
  }
}
