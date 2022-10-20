import {Component, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import { AuthService } from 'src/app/core/auth/auth.service';

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
        private fromBuilder: FormBuilder,
        private authService: AuthService,
    ) {
    }

    ngOnInit(): void {
    }

    login() {
        const username = this.formLogin.controls['username'].value;
        const password = this.formLogin.controls['password'].value;

        this.authService.login(username, password).subscribe(console.log);
    }
}
