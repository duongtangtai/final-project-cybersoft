import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';
import { IStaffModel } from 'src/app/model/staff.model';
import { MyToastrService } from 'src/app/share/services/my-toastr.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  
  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService
  ) {}

  updateProfile(profile: IStaffModel): Observable<IStaffModel>{
    console.log("updating profile in profile service")
    console.log(profile)
    console.log(this.config.endpoints.profile.root)
    return this.http.put<IRequestModel>(`${this.config.endpoints.profile.root}`, profile)
      .pipe(map((val: IRequestModel) => val.content));
  }
}
