import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { NgApexchartsModule } from "ng-apexcharts";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    DashboardRoutingModule,
    NgApexchartsModule,
  ],
  declarations: [DashboardComponent],
})
export class DashboardModule {}
