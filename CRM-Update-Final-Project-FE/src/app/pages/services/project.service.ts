import { AppSettings } from './../../app.constants';
import { MyToastrService } from './../../share/services/my-toastr.service';
import {HttpClient} from "@angular/common/http";
import {Inject, Injectable} from '@angular/core';
import {map, Observable} from "rxjs";
import {APP_CONFIG, PTSAppConfig} from "../../core/config/app.config";
import {IRequestModel} from "../../core/request/request.model";
import {IProjectModel} from "../../model/project.model";

@Injectable({
    providedIn: 'root'
})
export class ProjectService {
    private ADD_CREATOR = "/add-creator";
    private ADD_LEADER = "/add-leader"

    constructor(
        private http: HttpClient,
        @Inject(APP_CONFIG) private config: PTSAppConfig,
        private myToastrService: MyToastrService
    ) {}

    getProjects(): Observable<IProjectModel> {
        return this.http.get<IRequestModel>(`${this.config.endpoints.project.root}`)
          .pipe(map((val: IRequestModel) => val.content));
    }

    getProjectStatus(): Observable<string> {
        return this.http.get<IRequestModel>(`${this.config.endpoints.project.getStatus}`)
          .pipe(map((val: IRequestModel) => val.content));
    }

    saveProject(project: IProjectModel) {
        this.http.post<IRequestModel>(`${this.config.endpoints.project.root}`, project)
            .subscribe({
                next: result => this.myToastrService.success(result.content),
                error: exception => this.myToastrService.error(exception.error.errors)
            })
    }

    updateProject(project: IProjectModel) {
        this.http.put<IRequestModel>(`${this.config.endpoints.project.root}`, project)
        .subscribe({
            next: result => this.myToastrService.success(result.content),
            error: exception => this.myToastrService.error(exception.error.errors)
        })
    }   

    deleteProject(projectId: string) {
        return this.http.delete<IRequestModel>(`${this.config.endpoints.project.root}`+projectId)
            .subscribe({
                next: result => 
                    this.myToastrService.success(result.content),
                error: exception => 
                    this.myToastrService.error(exception.error.error)
            })
    }
}
