import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {StaffRoutingModule} from './staff-routing.module';
import {StaffComponent} from "./staff.component";

@NgModule({
    imports: [
        CommonModule, ReactiveFormsModule, FormsModule,
        StaffRoutingModule,
    ],
    declarations: [StaffComponent],
})
export class StaffModule {
}
