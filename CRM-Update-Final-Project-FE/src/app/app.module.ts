import { CommonModule } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { NgxWebstorageModule } from "ngx-webstorage";
import { AppRoutingModule } from "./app-routing.module";
import { AsideComponent } from "./layout/aside/aside.component";
import { FooterComponent } from "./layout/footer/footer.component";

import { HeaderComponent } from "./layout/header/header.component";
import { MainComponent } from "./layout/main/main.component";

@NgModule({
    declarations: [
        AsideComponent,
        HeaderComponent,
        FooterComponent,
        MainComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        CommonModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        NgxWebstorageModule.forRoot()
    ],
    bootstrap: [MainComponent]
})
export class AppModule {
}
