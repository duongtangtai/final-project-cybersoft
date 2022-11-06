import { MyToastrService } from './../../share/services/my-toastr.service';
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
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService
) {}

  getStaffs(): Observable<IStaffModel> {  
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.root}`)
        .pipe(map((val: IRequestModel) => val.content));
  }

  getStaffById(staffId: string): Observable<IStaffModel> {  
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.root}` + staffId)
        .pipe(map((val: IRequestModel) => val.content));
  }

  getStatus(): Observable<string> {
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.getStatus}`)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getGenders(): Observable<string> {
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.getGenders}`)
      .pipe(map((val: IRequestModel) => val.content));
  }

  saveStaff(staff: IStaffModel): Observable<IStaffModel> {
    return this.http.post<IRequestModel>(`${this.config.endpoints.staff.root}`, staff)
      .pipe(map((val: IRequestModel) => val.content));
  }

  updateStaff(staff: IStaffModel): Observable<IStaffModel>{
    return this.http.put<IRequestModel>(`${this.config.endpoints.staff.root}`, staff)
      .pipe(map((val: IRequestModel) => val.content));
  }

  deleteStaff(staffId: String): Observable<IStaffModel>{
    return this.http.delete<IRequestModel>(`${this.config.endpoints.staff.root}` + staffId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getStaffsInsideProject(projectId: string): Observable<IStaffModel> {
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.insideProject}` + projectId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getStaffsOutsideProject(projectId: string): Observable<IStaffModel>{
    return this.http.get<IRequestModel>(`${this.config.endpoints.staff.outsideProject}` + projectId)
      .pipe(map((val: IRequestModel) => val.content));
  }
}
