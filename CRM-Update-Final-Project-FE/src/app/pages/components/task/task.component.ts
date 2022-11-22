import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { filter } from 'rxjs';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ITaskModel } from 'src/app/model/task.model';
import { AppSettings } from '../../../app.constants';
import { DialogFormComponent } from '../../../share/components/dialog-form/dialog-form.component';
import { DialogNotifyComponent } from '../../../share/components/dialog-notify/dialog-notify.component';
import { ProfileService } from '../../services/profile.service';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss'],
})
export class TaskComponent implements OnInit {
  dataSource: any;
  tasks: any;
  appSettings = AppSettings;
  page: string = AppSettings.PATH_TASK || AppSettings.PATH_MY_TASK;

  statusTask: string = '';

  displayedColumns: string[] = ['name', 'projectName', 'status', 'action'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
    private localStorageService: LocalStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.page = this.router.url.substring(1);
    this.getAllTasks();
    //IF THIS IS MY TASK => THIS IS FOR EMPLOYEE
    //OTHER WISE => THIS IS FOR LEADER OR MANAGER
  }

  pagingAndSorting() {
    this.dataSource = new MatTableDataSource<ITaskModel>(this.tasks);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllTasks() {
    //Get tasks based on page
    this.statusTask = '';
    switch (
      this.page //page "TASKS"
    ) {
      case AppSettings.PATH_TASK:
        //CASE MANAGER -> show all tasks
        if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
          this.taskService.getTasksWithInfo().subscribe((content) => {
            this.tasks = content;
            this.pagingAndSorting();
          });
        } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) {
          //CASE LEADER -> show tasks belong to their projects
          this.taskService.getTasksWithInfo().subscribe((content) => {
            const leaderUsername = this.localStorageService.retrieve(
              AppSettings.AUTH_DATA
            ).userData.username;
            this.tasks = content.filter(
              (task: any) => task.project.leaderUsername == leaderUsername
            );
            this.pagingAndSorting();
          });
        }
        break;
      case AppSettings.PATH_MY_TASK: //page "MY-TASKS"
        //EMPLOYEE -> fetch all TASKs that they are reporting
        this.taskService.getTasksWithInfo().subscribe((content) => {
          const reporterUsername = this.localStorageService.retrieve(
            AppSettings.AUTH_DATA
          ).userData.username;
          this.tasks = content.filter(
            (task: any) => task.reporter.username == reporterUsername
          );
          this.pagingAndSorting();
        });
        break;
    }
  }

  getAllTasksWithStatus(status: string) {
    this.statusTask = status;
    switch (this.page) {
      case AppSettings.PATH_TASK:
        //MANAGER
        if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
          this.taskService.getTasksWithInfo().subscribe((content) => {
            this.tasks = content.filter((task: any) => task.status == status);
            this.pagingAndSorting();
          });
        } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) {
          //CASE LEADER -> show tasks belong to their projects
          this.taskService.getTasksWithInfo().subscribe((content) => {
            const leaderUsername = this.localStorageService.retrieve(
              AppSettings.AUTH_DATA
            ).userData.username;
            this.tasks = content.filter(
              (task: any) =>
                task.project.leaderUsername == leaderUsername &&
                task.status == status
            );
            this.pagingAndSorting();
          });
        }
        break;
      case AppSettings.PATH_MY_TASK:
        this.taskService.getTasksWithInfo().subscribe((content) => {
          const reporterUsername = this.localStorageService.retrieve(
            AppSettings.AUTH_DATA
          ).userData.username;
          this.tasks = content.filter(
            (task: any) =>
              task.reporter.username == reporterUsername &&
              task.status == status
          );
          this.pagingAndSorting();
        });
        break;
    }
  }

  filterByKeyword(event: Event) {
    const keyword = (event.target as HTMLInputElement).value;
    this.dataSource.filter = keyword;
  }

  addTask() {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_ADD_TASK,
          type: AppSettings.TYPE_TASK,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  updateTask(task: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_UPDATE_TASK,
          type: AppSettings.TYPE_TASK,
          element: task,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  deleteTask(taskId: any) {
    this.dialog
      .open(DialogNotifyComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_DELETE_TASK,
          message: AppSettings.MESSAGE_DELETE_TASK,
          id: taskId,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  commentTask(task: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          type: AppSettings.TYPE_COMMENT,
          title: AppSettings.TITLE_ADD_COMMENT,
          element: task,
        },
        height: '80%',
        width: '75%',
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  workWithTask(taskId: any) {
    this.dialog
      .open(DialogNotifyComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_IN_PROGRESS_TASK,
          message: AppSettings.MESSAGE_IN_PROGRESS_TASK,
          id: taskId,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  completeTask(taskId: any) {
    this.dialog
      .open(DialogNotifyComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_COMPLETE_TASK,
          message: AppSettings.MESSAGE_COMPLETE_TASK,
          id: taskId,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  taskDetail(task: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_TASK_DETAIL,
          type: AppSettings.TYPE_TASK,
          element: task,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusTaskAfterClosePopup());
  }

  statusTaskAfterClosePopup() {
    return this.statusTask
      ? this.getAllTasksWithStatus(this.statusTask)
      : this.getAllTasks();
  }
}
