import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ProjectRoutingModule } from "./project-routing.module";
import { ProjectComponent } from "./project.component";

@NgModule({
  declarations: [
    ProjectComponent
  ],
  imports: [
    CommonModule,
    ProjectRoutingModule
  ]
})
export class ProjectModule {
}
