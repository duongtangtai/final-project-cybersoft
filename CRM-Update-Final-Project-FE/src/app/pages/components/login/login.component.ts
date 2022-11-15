import { MyToastrService } from 'src/app/share/services/my-toastr.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {NavigationEnd, Router, RoutesRecognized} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {filter, pairwise} from "rxjs";
import { AuthService } from 'src/app/core/auth/auth.service';
import {AppSettings} from "../../../app.constants";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    formLogin = this.fromBuilder.nonNullable.group({
        username: '',
        password: ''
    })

    constructor(
        private router: Router,
        private fromBuilder: FormBuilder,
        private authService: AuthService,
        private localStorageService: LocalStorageService,
        private myToastrService: MyToastrService,
    ) {
    }

    ngOnInit(): void {}

    login() {
        const username = this.formLogin.controls['username'].value;
        const password = this.formLogin.controls['password'].value;

        this.authService.login(username, password).subscribe(val => {
            if (val) {
                this.router.navigateByUrl(AppSettings.PATH_PROFILE).then(r => console.log)
                this.myToastrService.info("Welcome " + username)            
            }
        });
    }

}
