import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {ITaskModel} from 'src/app/model/task.model';
import {AppSettings} from "../../../app.constants";
import {DialogFormComponent} from "../../../share/components/dialog-form/dialog-form.component";
import {DialogNotifyComponent} from "../../../share/components/dialog-notify/dialog-notify.component";
import {ProfileService} from "../../services/profile.service";
import {TaskService} from '../../services/task.service';

@Component({
    selector: 'app-task',
    templateUrl: './task.component.html',
    styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit {
    dataSource: any;
    tasks: any;
    taskStatus: string[] = ['UNASSIGNED', 'STARTED', 'COMPLETED'];

    displayedColumns: string[] = ['name', 'projectName', 'status', 'action'];
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;


    constructor(
        private taskService: TaskService,
        private dialog: MatDialog,
    ) {
    }

    ngOnInit(): void {
        this.getAllTasks();
    }

    pagingAndSorting() {
        this.dataSource = new MatTableDataSource<ITaskModel>(this.tasks)
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    getAllTasks() {
        this.taskService.getTasks().subscribe(result => {
            this.tasks = result;
            this.pagingAndSorting()
        });
    }

    getAllTasksWithStatus(status: string) {
        this.taskService.getTasks().subscribe(result => {
            this.tasks = result;
            switch (status) {
                case this.taskStatus[0]:
                    this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[0])
                    break;
                case this.taskStatus[1]:
                    this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[1])
                    break;
                case this.taskStatus[2]:
                    this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[2])
                    break;
            }
            this.pagingAndSorting()
        });
    }

    filterByKeyword(event: Event) {
        const keyword = (event.target as HTMLInputElement).value;
        this.dataSource.filter = keyword;
    }

    addTask() {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_ADD_TASK,
                type: AppSettings.TYPE_TASK,
            },
        }).afterClosed().subscribe(() => this.getAllTasks())
    }

    updateTask(task: any) {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_UPDATE_TASK,
                type: AppSettings.TYPE_TASK,
                element: task,
            },
        }).afterClosed().subscribe(() => this.getAllTasks())
    }

    deleteTask(taskId: any) {
        this.dialog.open(DialogNotifyComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_DELETE_TASK,
                message: AppSettings.MESSAGE_DELETE_TASK,
                id: taskId,
            },
        }).afterClosed().subscribe(() => this.getAllTasks())
    }
}

