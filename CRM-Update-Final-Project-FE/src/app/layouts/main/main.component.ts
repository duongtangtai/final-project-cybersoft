import {Component, OnInit} from '@angular/core';
import {LocalStorageService} from "ngx-webstorage";
import {AuthService} from "../../core/auth/auth.service";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {

    isLogin: boolean = false;

    constructor(
        private authService: AuthService,
        private localStorageService: LocalStorageService
    ) {
    }

    ngOnInit() {
        this.authService.isAuthenticated.subscribe(isAuth => {
            this.isLogin = isAuth;
        })
    }

}
