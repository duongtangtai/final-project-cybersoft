import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';
import { IRoleModel } from 'src/app/model/role.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  constructor(    
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    ) 
    { }
  
  getRoles(): Observable<IRoleModel> {  
    return this.http.get<IRequestModel>(`${this.config.endpoints.role.root}`)
        .pipe(map((val: IRequestModel) => val.content));
  }
}
