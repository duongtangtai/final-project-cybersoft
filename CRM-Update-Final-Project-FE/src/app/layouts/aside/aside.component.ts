import { AppSettings } from 'src/app/app.constants';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-aside',
  templateUrl: './aside.component.html',
  styleUrls: ['./aside.component.scss']
})
export class AsideComponent implements OnInit {
  appSettings = AppSettings;

  menu = {
    "tasks" : false
  }

  openMenu(menuName: string) {
    switch(menuName) {
      case "tasks":
        this.menu.tasks = !this.menu.tasks;
        break;
      default:
        break;
    }
  }

  constructor() { }

  ngOnInit(): void {
  }
}
