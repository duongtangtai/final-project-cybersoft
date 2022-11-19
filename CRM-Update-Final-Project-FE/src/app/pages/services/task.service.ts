import { MyToastrService } from './../../share/services/my-toastr.service';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { IRequestModel } from 'src/app/core/request/request.model';
import { ITaskModel } from 'src/app/model/task.model';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService
  ) {}

  getTasks(): Observable<ITaskModel> {
    return this.http
      .get<IRequestModel>(`${this.config.endpoints.task.root}`)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getTasksWithInfo() {
    return this.http
      .get<IRequestModel>(`${this.config.endpoints.task.withInfo}`)
      .pipe(map((val: IRequestModel) => val.content));
  }

  getStatus(): Observable<ITaskModel> {
    return this.http
      .get<IRequestModel>(`${this.config.endpoints.task.getStatus}`)
      .pipe(map((val: IRequestModel) => val.content));
  }

  saveTask(task: ITaskModel): Observable<ITaskModel> {
    return this.http
      .post<IRequestModel>(`${this.config.endpoints.task.root}`, task)
      .pipe(map((val: IRequestModel) => val.content));
  }

  updateTask(task: ITaskModel): Observable<ITaskModel> {
    return this.http
      .put<IRequestModel>(`${this.config.endpoints.task.root}`, task)
      .pipe(map((val: IRequestModel) => val.content));
  }

  deleteTask(taskId: String): Observable<ITaskModel> {
    return this.http
      .delete<IRequestModel>(`${this.config.endpoints.task.root}` + taskId)
      .pipe(map((val: IRequestModel) => val.content));
  }

  workWithTask(taskId: String): Observable<ITaskModel> {
    return this.http
      .post<IRequestModel>(
        `${this.config.endpoints.task.workWithTask}` + taskId,
        ''
      )
      .pipe(map((val: IRequestModel) => val.content));
  }

  completeTask(taskId: String): Observable<ITaskModel> {
    return this.http
      .post<IRequestModel>(
        `${this.config.endpoints.task.completeTask}` + taskId,
        ''
      )
      .pipe(map((val: IRequestModel) => val.content));
  }
}
