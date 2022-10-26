import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {DialogComponent} from "./components/dialog/dialog.component";
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
    declarations: [DialogComponent],
})
export class ShareModule {
}
