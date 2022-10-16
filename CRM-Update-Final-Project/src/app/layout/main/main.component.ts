import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../core/service/auth.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  constructor(
      readonly authService: AuthService
  ) {
  }

  ngOnInit(): void {
  }

}
