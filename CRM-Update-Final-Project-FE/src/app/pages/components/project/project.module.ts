import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ProjectRoutingModule} from './project-routing.module';
import {ProjectComponent} from "./project.component";

@NgModule({
    imports: [
        CommonModule, ReactiveFormsModule, FormsModule,
        ProjectRoutingModule,
    ],
    declarations: [ProjectComponent],
})
export class ProjectModule {
}
