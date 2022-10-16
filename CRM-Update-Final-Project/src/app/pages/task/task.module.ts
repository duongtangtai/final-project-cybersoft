import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {TaskRoutingModule} from "./task-routing.module";
import {TaskComponent} from "./task.component";

@NgModule({
    declarations: [
        TaskComponent
    ],
    imports: [
        CommonModule,
        TaskRoutingModule,
    ]
})
export class TaskModule {
}
