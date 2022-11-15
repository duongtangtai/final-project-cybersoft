import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { LocalStorageService } from 'ngx-webstorage';
import { AppSettings } from 'src/app/app.constants';
import { AuthService } from "../../core/auth/auth.service";
import { ProfileService } from "../../pages/services/profile.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    user: any;
    avatar: string = '';
    gender: string = '';
    firstName: string = '';
    lastName: string = '';
    roles: string[] = [];

    constructor(
        private router: Router,
        private authService: AuthService,
        private profileService: ProfileService,
        private localStorageService: LocalStorageService,
    ) {
    }

    ngOnInit(): void {
        this.user = this.localStorageService.retrieve(AppSettings.AUTH_DATA);
        this.getAvatarLink();
        this.getUserFirstName();
        this.getUserLastName();
        this.getUserRoles();
    }

    getAvatarLink() {
        this.profileService.avatarData.subscribe(avatarUrl => {
            if (avatarUrl) {
                this.avatar = avatarUrl;
            } else {
                //init avatar
                this.avatar = this.user.userData.avatar;
                this.gender = this.user.userData.gender;
            }
        });
    }

    getUserFirstName() {
        this.profileService.firstNameData.subscribe(firstName => {
            if (firstName) {
                this.firstName = firstName;
            } else {
                this.firstName = this.user.userData.firstName;
            }
        })
    }

    getUserLastName() {
        this.profileService.lastNameData.subscribe(lastName => {
            if (lastName) {
                this.lastName = lastName;
            } else {
                this.lastName = this.user.userData.lastName;
            }
        })
    }

    getUserRoles() {
        this.profileService.rolesData.subscribe((roleCodes: any) => {
            if (roleCodes) {
                this.roles = this.displayUserRoles(roleCodes);
                AppSettings.USER_ROLES = roleCodes;
            } else {
                this.roles = this.displayUserRoles(this.user.roleCodes);
                AppSettings.USER_ROLES = this.user.roleCodes;
            }
        })
    }

    getProfile() {
        this.router.navigateByUrl(AppSettings.PATH_PROFILE).then(r => console.log)
    }

    logout() {
        AppSettings.LOG_OUT = true;
        this.authService.logout();
        this.router.navigateByUrl(AppSettings.PATH_LOGIN).then(r => console.log);
    }

    displayUserRoles(roleCodes: string[]) {
        if (roleCodes.includes(AppSettings.ROLE_ADMIN)) {
            return ["ADMIN"]
        } else if (roleCodes.includes(AppSettings.ROLE_MANAGER)) {
            return ["MANAGER"]
        } else if (roleCodes.includes(AppSettings.ROLE_LEADER)) {
            return roleCodes.includes(AppSettings.ROLE_EMPLOYEE) ? ["LEADER", "EMPLOYEE"] : ["LEADER"]
        }
        return ["EMPLOYEE"]
    }
}
