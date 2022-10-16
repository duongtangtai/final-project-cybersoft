import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../core/service/auth.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    isOpenDropdownMenu: boolean = false;

    constructor(
        private authService: AuthService
    ) {
    }

    ngOnInit(): void {
    }

    onOpenDropdownMenu() {
        console.log("ok");
        this.isOpenDropdownMenu = !this.isOpenDropdownMenu;
    }
}
