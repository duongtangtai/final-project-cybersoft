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

    user: any;
    avatar: string = '';
    gender: string = '';
    firstName: string ='';
    lastName: string ='';
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
        this.profileService.rolesData.subscribe((roles: any)=> {
            if (roles) {
                this.roles = this.specifyUserRoles(roles);
            } else {
                this.roles = this.specifyUserRoles(this.user.roleCodes);
            }
            AppSettings.USER_ROLES = this.roles; //set roles for user
        })
    }

    getProfile() {
        this.router.navigateByUrl("/profile").then(r => console.log)
    }

    logout() {
        AppSettings.LOG_OUT = true;
        this.authService.logout();
        this.router.navigateByUrl('/login').then(r => console.log);
    }

    specifyUserRoles(userRoles: string[]) {
        if (userRoles.includes("AD")) {
            return ["ADMIN"]
        } else if (userRoles.includes("MGR")) {
            return ["MANAGER"]
        } else if (userRoles.includes("LEAD")) {
            return userRoles.includes("EMP") ? ["LEADER", "EMPLOYEE"] : ["LEADER"]
        }
        return ["EMPLOYEE"]
    }
}
