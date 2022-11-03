import { AppSettings } from './../../../app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import {CommonModule} from "@angular/common";
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LoginRoutingModule} from './login-routing.module';
import {LoginComponent} from "./login.component";
import { Router } from '@angular/router';

@NgModule({
    imports: [
        CommonModule, ReactiveFormsModule, FormsModule,
        LoginRoutingModule,
    ],
    declarations: [LoginComponent],
})
export class LoginModule { 
    constructor(
        private localStorageService: LocalStorageService,
        private router: Router,
    ) {
        this.localStorageService.retrieve(AppSettings.AUTH_DATA).accessToken ? 
        this.router.navigateByUrl("/project") : ''
    }
}
