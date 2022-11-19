import { AppSettings } from 'src/app/app.constants';
import { ProjectService } from 'src/app/pages/services/project.service';
import { Component, OnInit } from '@angular/core';
import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexTitleSubtitle,
} from 'ng-apexcharts';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  //Project
  projectTotal: number = 0;
  projectStatusDoing: number = 0;
  projectStatusDone: number = 0;

  //Task
  taskTotal: number = 0;
  taskStatusUnassigned: number = 0;
  taskStatusStarted: number = 0;
  taskStatusCompleted: number = 0;

  constructor(
    private projectService: ProjectService,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    //init project & task data to show charts
    this.projectService.getProjectsWithInfo().subscribe((content: any) => {
      this.projectTotal = content.length;
      this.projectStatusDoing = content.filter(
        (project: any) => project.status == AppSettings.PROJECT_STATUS_DOING
      ).length;
      this.projectStatusDone = content.filter(
        (project: any) => project.status == AppSettings.PROJECT_STATUS_DONE
      ).length;
      this.projectChartSeries = [
        this.projectStatusDoing,
        this.projectStatusDone,
      ];
    });
    this.taskService.getTasksWithInfo().subscribe((content: any) => {
      this.taskTotal = content.length;
      this.taskStatusUnassigned = content.filter(
        (task: any) => task.status == AppSettings.TASK_STATUS_TODO
      ).length;
      this.taskStatusStarted = content.filter(
        (task: any) => task.status == AppSettings.TASK_STATUS_IN_PROGRESS
      ).length;
      this.taskStatusCompleted = content.filter(
        (task: any) => task.status == AppSettings.TASK_STATUS_DONE
      ).length;
      this.taskChartSeries = [
        this.taskStatusUnassigned,
        this.taskStatusStarted,
        this.taskStatusCompleted,
      ];
    });
  }

  //------------------PROJECT CHART--------------------
  projectChartSeries: ApexNonAxisChartSeries = [];
  projectChartDetails: ApexChart = {
    width: 600,
    type: 'donut',
    toolbar: {
      show: true,
    },
  };
  projectChartColors = ['#0d6efd', '#198754'];
  projectChartLabels = [
    AppSettings.PROJECT_STATUS_DOING,
    AppSettings.PROJECT_STATUS_DONE,
  ];
  //----------------------------------------------------

  //------------------TASK CHART------------------------
  taskChartSeries: ApexNonAxisChartSeries = [];
  taskChartDetails: ApexChart = {
    width: 600,
    type: 'pie',
    toolbar: {
      show: true,
    },
  };
  taskChartColors = ['#0dcaf0', '#0d6efd', '#198754'];
  taskChartLabels = [
    AppSettings.TASK_STATUS_TODO,
    AppSettings.TASK_STATUS_IN_PROGRESS,
    AppSettings.TASK_STATUS_DONE,
  ];
  //----------------------------------------------------
}
