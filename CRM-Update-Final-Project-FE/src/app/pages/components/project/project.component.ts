import { Component, OnInit } from '@angular/core';
import {ProjectService} from "../../services/project.service";

@Component({
  selector: 'app-login',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {

  constructor(
      private projectService: ProjectService
  ) {
  }

  ngOnInit(): void {
    this.getAllProject();
  }

  private getAllProject() {
    this.projectService.getProjects().subscribe(console.log);
  }
}
