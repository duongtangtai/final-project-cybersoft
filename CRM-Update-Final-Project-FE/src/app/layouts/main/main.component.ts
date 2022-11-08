import { Component, DoCheck, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit, DoCheck {
  isLogin: boolean = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {}

  ngDoCheck(): void {
    if (
      this.router.url === '/403' ||
      this.router.url === '/404' ||
      this.router.url === '/500'
    ) {
      this.isLogin = false;
    } else {
      this.authService.isAuthenticated.subscribe((isAuth) => {
        this.isLogin = isAuth;
      });
    }
  }
}
