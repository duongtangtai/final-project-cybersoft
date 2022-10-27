import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {ButtonComponent} from './components/button/button.component';
import {DialogFormComponent} from "./components/dialog-form/dialog-form.component";
import {DialogNotifyComponent} from './components/dialog-notify/dialog-notify.component';
import {MaterialModule} from "./modules/material-module";

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        NgxWebstorageModule.forRoot(),
        ReactiveFormsModule,
        MaterialModule
    ],
    declarations: [DialogFormComponent, DialogNotifyComponent, DialogNotifyComponent, ButtonComponent],
})
export class ShareModule {
}
