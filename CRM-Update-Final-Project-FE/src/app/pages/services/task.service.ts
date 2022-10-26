import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';
import { ITaskModel } from 'src/app/model/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig
) {}

  getTasks(): Observable<ITaskModel> {  
    return this.http.get<IRequestModel>(`${this.config.endpoints.task.getAll}`)
        .pipe(map((val: IRequestModel) => val.content));
  }
}
