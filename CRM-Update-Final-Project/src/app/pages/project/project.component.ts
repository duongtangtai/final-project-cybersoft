import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../core/service/auth.service";

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {

  constructor(
      private authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    console.log(this.authService._isLogin);
  }

}
