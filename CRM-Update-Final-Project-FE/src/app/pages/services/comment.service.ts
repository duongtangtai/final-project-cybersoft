import { ICommentModel } from './../../model/comment.model';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { APP_CONFIG, PTSAppConfig } from 'src/app/core/config/app.config';
import { MyToastrService } from 'src/app/share/services/my-toastr.service';
import { map, Observable } from 'rxjs';
import { IRequestModel } from 'src/app/core/request/request.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(
    private http: HttpClient,
    @Inject(APP_CONFIG) private config: PTSAppConfig,
    private myToastrService: MyToastrService,
    private localStorageService: LocalStorageService,
  ) {}

  getCommentByTaskId(taskId: string): Observable<ICommentModel>{
    return this.http.get<IRequestModel>(`${this.config.endpoints.comment.getByTaskId}` + taskId)
        .pipe(map((val: IRequestModel) => val.content));
  }

  addComment(submitForm: any) : Observable<any>{
    return this.http.post<IRequestModel>(`${this.config.endpoints.comment.root}`, submitForm)
        .pipe(map((val: IRequestModel) => val.content));
  }
}
