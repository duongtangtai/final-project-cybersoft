import { IRequestModel } from 'src/app/core/request/request.model';
import { MyToastrService } from './../../share/services/my-toastr.service';
import {HttpClient} from "@angular/common/http";
import {Inject, Injectable} from '@angular/core';
import {map, Observable, of} from "rxjs";
import {APP_CONFIG, PTSAppConfig} from "../../core/config/app.config";
import {IProjectModel} from "../../model/project.model";

@Injectable({
    providedIn: 'root'
})
export class ProjectService {

    constructor(
        private http: HttpClient,
        @Inject(APP_CONFIG) private config: PTSAppConfig,
        private myToastrService: MyToastrService
    ) {}

    getProjects(): Observable<IProjectModel> {
        return this.http.get<IRequestModel>(`${this.config.endpoints.project.root}`)
            .pipe(map((val: IRequestModel) => val.content));
    }

    getProjectsWithInfo() {
        return this.http.get<IRequestModel>(`${this.config.endpoints.project.withInfo}`)
            .pipe(map((val: IRequestModel) => val.content));
    }

    getProjectStatus(): Observable<string> {
        return this.http.get<IRequestModel>(`${this.config.endpoints.project.getStatus}`)
            .pipe(map((val: IRequestModel) => val.content));
    }

    saveProject(project: IProjectModel): Observable<IProjectModel> {
        return this.http.post<IRequestModel>(`${this.config.endpoints.project.root}`, project)
            .pipe(map((val: IRequestModel) => val.content))
    }

    updateProject(project: IProjectModel): Observable<IProjectModel> {
        return this.http.put<IRequestModel>(`${this.config.endpoints.project.root}`, project)
            .pipe(map((val: IRequestModel) => val.content))
    }   

    deleteProject(projectId: string): Observable<IProjectModel> {
        return this.http.delete<IRequestModel>(`${this.config.endpoints.project.root}`+projectId)
            .pipe(map((val: IRequestModel) => val.content))
    }

    addStaffToProject(projectId: string, staffId: string) {
        return this.http.post<IRequestModel>(`${this.config.endpoints.project.addUsers}`+projectId,
            [staffId])
            .pipe(map((val: IRequestModel) => val.content))
    }

    removeStaffFromProject(projectId: string, staffId: string) {
        return this.http.post<IRequestModel>(`${this.config.endpoints.project.removeUsers}`+projectId,
            [staffId])
        .pipe(map((val: IRequestModel) => val.content))
    }
}
