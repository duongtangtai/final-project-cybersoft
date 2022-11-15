import { LocalStorageService } from 'ngx-webstorage';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { DialogNotifyComponent } from 'src/app/share/components/dialog-notify/dialog-notify.component';
import { AppSettings } from "../../../app.constants";
import { DialogFormComponent } from "../../../share/components/dialog-form/dialog-form.component";
import { ProjectService } from "../../services/project.service";
import { IProjectModel } from './../../../model/project.model';


@Component({
    selector: 'app-project',
    templateUrl: './project.component.html',
    styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
    dataSource: any;
    projects: any;
    appSettings = AppSettings;

    displayedColumns: string[] = [];
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;


    constructor(
        private projectService: ProjectService,
        private dialog: MatDialog,
        private localStorageService: LocalStorageService,
    ) {
    }

    ngOnInit(): void {
        this.getAllProject();
        this.initDisplayedColumns();
    }

    initDisplayedColumns() { //init display columns based on USER ROLES
        if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
            this.displayedColumns = ['symbol', 'name', 'description', 'status', 'action'];
        } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) {
            this.displayedColumns = ['symbol', 'name', 'description', 'status'];
        }
    }

    pagingAndSorting() {
        this.dataSource = new MatTableDataSource<IProjectModel>(this.projects)
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    getAllProject() {
        //CASE MANAGER
        if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
            this.projectService.getProjectsWithInfo().subscribe(result => {
                this.projects = result;
                this.pagingAndSorting()
            });
        } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) {
            //CASE LEADER => all projects that this leader is assigned
            this.projectService.getProjectsWithInfo().subscribe(result => {
                const leaderUsername = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
                    .userData.username;
                this.projects = result.filter((project: any) => project.leader.username == leaderUsername);
                this.pagingAndSorting()
            });
        }
    }

    getAllProjectWithStatus(status: string) {
        //CASE MANAGER
        if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
            this.projectService.getProjectsWithInfo().subscribe(content => {
                this.projects = content.filter((project: any) => project.status == status)
                this.pagingAndSorting()
            });
        } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) { //CASE LEADER
            this.projectService.getProjectsWithInfo().subscribe(content => {
                const leaderUsername = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
                    .userData.username;
                this.projects = content
                    .filter((project: any) => project.leader.username == leaderUsername
                        && project.status == status)
                this.pagingAndSorting()
            });
        }
    }

    filterByKeyword(event: Event) {
        const keyword = (event.target as HTMLInputElement).value;
        this.dataSource.filter = keyword;
    }

    addProject() {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.TITLE_ADD_PROJECT,
                type: AppSettings.TYPE_PROJECT,
            },
        }).afterClosed().subscribe(() => this.getAllProject())
    }

    updateProject(project: any) {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.TITLE_UPDATE_PROJECT,
                type: AppSettings.TYPE_PROJECT,
                element: project,
            },
        }).afterClosed().subscribe(() => this.getAllProject())
    }

    deleteProject(projectId: any) {
        this.dialog.open(DialogNotifyComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.TITLE_DELETE_PROJECT,
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

    showProjectDetail(project: any) {
        //SHOW PROJECT DETAIL
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.TITLE_PROJECT_DETAIL,
                type: AppSettings.TYPE_PROJECT,
                element: project,
            },
            width: "75%",
            height: "85%",
        }).afterClosed().subscribe(() => this.getAllProject())
    }
}
