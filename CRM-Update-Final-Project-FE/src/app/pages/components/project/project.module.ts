import { MaterialModule } from 'src/app/share/modules/material-module';
import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ShareModule} from "../../../share/share.module";
import {ProjectRoutingModule} from './project-routing.module';
import {ProjectComponent} from "./project.component";
import { ActivatedRoute, Router } from '@angular/router';

@NgModule({
    imports: [
        CommonModule, ReactiveFormsModule, FormsModule,
        ProjectRoutingModule,
        MaterialModule
    ],
    declarations: [ProjectComponent],
})
export class ProjectModule {
}
