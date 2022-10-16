import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {LoginComponent} from '../account/login/login.component';
import {RegisterComponent} from '../account/register/register.component';
import {UserRoutingModule} from "./user-routing.module";
import {UserComponent} from "./user.component";

@NgModule({
    declarations: [UserComponent, LoginComponent, RegisterComponent],
    imports: [
        CommonModule,
        UserRoutingModule,
        ReactiveFormsModule
    ]
})
export class UserModule {
}
