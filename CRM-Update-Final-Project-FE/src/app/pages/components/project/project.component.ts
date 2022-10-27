import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {AppSettings} from "../../../app.constants";
import {DialogComponent} from "../../../share/components/dialog-form/dialog-form.component";
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
        private dialog: MatDialog
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
        this.projectService.getProjects().subscribe(result => {
            this.projects = result;
            this.pagingAndSorting()
        });
    }

    getAllProjectWithStatus(status: string) {
        this.projectService.getProjects().subscribe(result => {
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

    updateProject(projectId: any) {
        console.log("update project with ID: " + projectId)
    }

    deleteProject(projectId: any) {
        console.log("delete project with ID: " + projectId)
    }

    onAddProject() {
        const addProjectDialog = this.dialog.open(DialogComponent, {
          data: {
            title: AppSettings.TITLE_ADD_PROJECT,
            type: AppSettings.TYPE_PROJECT,
          }
        })
    }
}
