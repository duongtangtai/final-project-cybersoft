import { AppSettings } from 'src/app/app.constants';
import { ProjectService } from 'src/app/pages/services/project.service';
import { Component, OnInit } from '@angular/core';
import {
  ApexChart,
  ApexNonAxisChartSeries,
  ApexTitleSubtitle,
} from 'ng-apexcharts';

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

  constructor(
    private projectService: ProjectService,
  ) {
    //init project
    this.projectService.getProjectsWithInfo().subscribe((content: any) => {
      this.projectTotal = content.length;
      this.projectStatusDoing = content.filter(
        (project: any) => project.status == AppSettings.PROJECT_STATUS_DOING
      ).length;
      this.projectStatusDone = content.filter(
        (project: any) => project.status == AppSettings.PROJECT_STATUS_DONE
      ).length;
      this.projectChartLabels = [
        AppSettings.PROJECT_STATUS_DOING,
        AppSettings.PROJECT_STATUS_DONE,
      ]
      this.projectChartSeries = [
        this.projectStatusDoing,
        this.projectStatusDone,
      ];
    });
  }

  ngOnInit(): void {
  }

  //------------------PROJECT CHART--------------------
  projectChartSeries: ApexNonAxisChartSeries = [];
  projectChartDetails: ApexChart = {
    width: 600,
    type: 'donut',
    toolbar: {
      show: true,
    },
    animations: {
      enabled: true,
    }
  };
  projectChartColors = ['#0d6efd', '#198754'];
  projectChartLabels: string[] = [];
  //----------------------------------------------------
}
