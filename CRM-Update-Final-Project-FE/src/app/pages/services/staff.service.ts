import { IStaffModel } from './../../model/staff.model';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';

@Injectable({
  providedIn: 'root'
})
export class StaffService {
  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig
) {}

  getStaffs(): Observable<IStaffModel> {  
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.getAll}`)
        .pipe(map((val: IRequestModel) => val.content));
  }
}
