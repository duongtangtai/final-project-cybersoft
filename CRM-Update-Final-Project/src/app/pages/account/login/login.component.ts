import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {LocalStorageService} from "ngx-webstorage";
import {AuthService} from "../../../core/service/auth.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    formLogin!: FormGroup;

    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private localSt: LocalStorageService
    ) {
        this.formLogin = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        })
    }

    ngOnInit(): void {
        this.localSt.observe('key')
            .subscribe(val => console.log('new value: ', val));
    }

    login() {
        const userName = this.formLogin.controls['username'].value;
        const passWord = this.formLogin.controls['password'].value;

        if (userName == 'admin' && passWord == 'admin') {
            this.router.navigate(['/project']).then(r => this.authService._isLogin.next(true));
        } else {
            this.router.navigate(['/account/login']).then(r => this.authService._isLogin.next(false));
        }
    }

}
