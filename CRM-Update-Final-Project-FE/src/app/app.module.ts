import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {AppRoutingModule} from './app-routing.module';
import {APP_CONFIG, AppConfig} from "./core/config/app.config";
import {TokenInterceptor} from "./core/interceptor/token.interceptor";
import {AsideComponent} from './layouts/aside/aside.component';
import {ErrorComponent} from './layouts/error/error.component';
import {FooterComponent} from './layouts/footer/footer.component';
import {HeaderComponent} from './layouts/header/header.component';
import {MainComponent} from './layouts/main/main.component';

@NgModule({
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgxWebstorageModule.forRoot(),
    ],
    providers: [
        {provide: APP_CONFIG, useValue: AppConfig},
        {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}
    ],
    declarations: [MainComponent, ErrorComponent, FooterComponent, HeaderComponent, AsideComponent],
    bootstrap: [MainComponent],
})
export class AppModule {
}
