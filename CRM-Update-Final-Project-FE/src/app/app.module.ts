import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {MaterialModule} from 'src/app/share/modules/material-module';
import {AppRoutingModule} from './app-routing.module';
import {APP_CONFIG, AppConfig} from "./core/config/app.config";
import { HttpErrorInterceptor } from './core/interceptor/error-handle.interceptor';
import {TokenInterceptor} from "./core/interceptor/token.interceptor";
import {AsideComponent} from './layouts/aside/aside.component';
import {ErrorComponent} from './layouts/error/error.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {HeaderComponent} from './layouts/header/header.component';
import {MainComponent} from './layouts/main/main.component';
import {ShareModule} from "./share/share.module";

export const MY_DATE_FORMATS  = {
    parse: {
      dateInput: 'LL',
    },
    display: {
      dateInput: 'YYYY-MM-DD',
      monthYearLabel: 'YYYY',
      dateA11yLabel: 'LL',
      monthYearA11yLabel: 'YYYY',
    },
};

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HttpClientModule,
        NgxWebstorageModule.forRoot(),
        ReactiveFormsModule,
        MaterialModule,
        ShareModule
    ],
    providers: [
        {provide: APP_CONFIG, useValue: AppConfig},
        {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
    ],
    declarations: [MainComponent, ErrorComponent, FooterComponent, HeaderComponent, AsideComponent],
    bootstrap: [MainComponent],
})
export class AppModule {
}
