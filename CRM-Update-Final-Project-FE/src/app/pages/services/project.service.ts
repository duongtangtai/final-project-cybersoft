import {HttpClient} from "@angular/common/http";
import {Inject, Injectable} from '@angular/core';
import {map, Observable} from "rxjs";
import {APP_CONFIG, PTSAppConfig} from "../../core/config/app.config";
import {IRequestModel} from "../../core/request/request.model";
import {IProject} from "../../model/project.model";

@Injectable({
    providedIn: 'root'
})
export class ProjectService {

    constructor(
        private http: HttpClient,
        @Inject(APP_CONFIG) private config: PTSAppConfig
    ) {}

    getProjects(): Observable<IProject> {
      return this.http.get<IRequestModel>(`${this.config.endpoints.project.getAll}`)
          .pipe(map((val: IRequestModel) => val.content));
    }
}
