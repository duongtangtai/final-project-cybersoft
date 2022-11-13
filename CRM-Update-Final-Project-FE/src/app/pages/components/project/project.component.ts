import { LocalStorageService } from 'ngx-webstorage';
import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import {DialogNotifyComponent} from 'src/app/share/components/dialog-notify/dialog-notify.component';
import {AppSettings} from "../../../app.constants";
import {DialogFormComponent} from "../../../share/components/dialog-form/dialog-form.component";
import {ProjectService} from "../../services/project.service";
import {IProjectModel} from './../../../model/project.model';


@Component({
    selector: 'app-project',
    templateUrl: './project.component.html',
    styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
    dataSource: any;
    projects: any;
    projectStatus: string[] = ['DOING', 'DONE']

    displayedColumns: string[] = ['symbol', 'name', 'description', 'status', 'action'];
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;


    constructor(
        private projectService: ProjectService,
        private dialog: MatDialog,
    ) {
    }

    ngOnInit(): void {
        this.getAllProject();
    }

    pagingAndSorting() {
        this.dataSource = new MatTableDataSource<IProjectModel>(this.projects)
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    getAllProject() {
        this.projectService.getProjectsWithInfo().subscribe(result => {
            this.projects = result;
            this.pagingAndSorting()
        });
    }

    getAllProjectWithStatus(status: string) {
        this.projectService.getProjectsWithInfo().subscribe(result => {
            this.projects = result;
            switch (status) {
                case this.projectStatus[0]:
                    this.projects = this.projects.filter((element: any) => element.status == this.projectStatus[0])
                    break;
                case this.projectStatus[1]:
                    this.projects = this.projects.filter((element: any) => element.status == this.projectStatus[1])
                    break;
            }
            this.pagingAndSorting()
        });
    }

    filterByKeyword(event: Event) {
        const keyword = (event.target as HTMLInputElement).value;
        this.dataSource.filter = keyword;
    }

    addProject() {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_ADD_PROJECT,
                type: AppSettings.TYPE_PROJECT,
            },
        }).afterClosed().subscribe(() => this.getAllProject())
    }

    updateProject(project: any) {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_UPDATE_PROJECT,
                type: AppSettings.TYPE_PROJECT,
                element: project,
            },
        }).afterClosed().subscribe(() => this.getAllProject())
    }

    deleteProject(projectId: any) {
        this.dialog.open(DialogNotifyComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_DELETE_PROJECT,
                message: AppSettings.MESSAGE_DELETE_PROJECT,
                id: projectId,
            },
        }).afterClosed().subscribe(() => this.getAllProject())
    }

    manageStaffs(project: any) {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.TITLE_CURRENT_STAFF,
                type: AppSettings.TYPE_MANAGE_STAFF_IN_PROJECT,
                element: project,
            },
            width: "75%",
            height: "85%",
        }).afterClosed().subscribe(() => this.getAllProject())
    }
}
