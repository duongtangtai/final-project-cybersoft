import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {LocalStorageService} from 'ngx-webstorage';
import {AppSettings} from 'src/app/app.constants';
import {AuthService} from "../../core/auth/auth.service";
import {ProfileService} from "../../pages/services/profile.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    avatar: string = '';
    gender: string = '';

    constructor(
        private router: Router,
        private authService: AuthService,
        private profileService: ProfileService,
        private localStorageService: LocalStorageService,
    ) {
    }

    ngOnInit(): void {
        this.getAvatarLink();
    }

    getAvatarLink() {
        this.profileService.data.subscribe(avatarLink => {
            if (avatarLink) {
                this.avatar = avatarLink;
            } else {
                //init avatar
                const userData = this.localStorageService.retrieve(AppSettings.AUTH_DATA).userData;
                this.avatar = userData.avatar;
                this.gender = userData.gender;
            }
        });
    }

    getProfile() {
        this.router.navigateByUrl("/profile").then(r => console.log)
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('/login').then(r => console.log);
    }
}
