import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../core/auth/auth.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    constructor(
        private router: Router,
        private authService: AuthService
    ) {
    }

    ngOnInit(): void {
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('/login').then(r => console.log);
    }
}
