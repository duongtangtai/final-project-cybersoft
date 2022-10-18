import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TaskRoutingModule} from './task-routing.module';
import {TaskComponent} from "./task.component";

@NgModule({
    imports: [
        CommonModule, ReactiveFormsModule, FormsModule,
        TaskRoutingModule,
    ],
    declarations: [TaskComponent],
})
export class TaskModule {
}
