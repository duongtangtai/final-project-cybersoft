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

  private avatarUrl: BehaviorSubject<string> = new BehaviorSubject<string>('');
  avatarData: Observable<string> = this.avatarUrl.asObservable();
  private firstName: BehaviorSubject<string> = new BehaviorSubject<string>('');
  firstNameData: Observable<string> = this.firstName.asObservable();
  private lastName: BehaviorSubject<string> = new BehaviorSubject<string>('');
  lastNameData: Observable<string> = this.lastName.asObservable();
  private roles: BehaviorSubject<string> = new BehaviorSubject<string>('');
  rolesData: Observable<string> = this.roles.asObservable();


  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService,
    private localStorageService: LocalStorageService,
  ) {}

  updateProfile(profile: IStaffModel): Observable<IStaffModel>{
    return this.http.put<IRequestModel>(`${this.config.endpoints.profile.updateProfile}`, profile)
      .pipe(map((val: IRequestModel) => val.content));
  }

  uploadAvatar(submitForm: any): Observable<any> {
    return this.http.post(`${this.config.endpoints.file.root}`, submitForm)
      .pipe(map((val: any) => val.content));
  }

  changePassword(submitForm: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    }
    return this.http.put(`${this.config.endpoints.profile.changePassword}`, submitForm)
      .pipe(map((val: any) => val.content));
  }

  sendAvatarUrl(avatarUrl: string) {
    this.avatarUrl.next(avatarUrl);
  }

  sendFirstName(firstName: string) {
    this.firstName.next(firstName)
  }

  sendLastName(lastName: string) {
    this.lastName.next(lastName)
  }

  sendRoles(roles: string[]) {
    this.roles.next(roles.toString())
  }
}
