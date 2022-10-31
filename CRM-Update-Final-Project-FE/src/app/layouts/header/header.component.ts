import { AppSettings } from 'src/app/app.constants';
import { LocalStorageService } from 'ngx-webstorage';
import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../core/auth/auth.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    avatar: string ='';

    constructor(
        private router: Router,
        private authService: AuthService,
        private localStorageService: LocalStorageService,
    ) {
    }

    ngOnInit(): void { 
        //init avatar
        this.avatar = this.localStorageService.retrieve(AppSettings.AUTH_DATA).avatar;
    }

    getProfile() {
        this.router.navigateByUrl("/profile").then(r => console.log)
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('/login').then(r => console.log);
    }
}
