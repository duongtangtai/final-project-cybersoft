import { IProject } from './../../../model/project.model';
import { MatPaginator} from '@angular/material/paginator';
import { Component, OnInit, ViewChild } from '@angular/core';
import {ProjectService} from "../../services/project.service";
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {
  dataSource: any;
  projects: any;
  
  displayedColumns: string[] = ['name', 'description', 'symbol', 'action'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(
      private projectService: ProjectService
  ) {}

  ngOnInit(): void {
    this.getAllProject(); 
  }

  private getAllProject() {
    this.projectService.getProjects().subscribe(result => {
      this.projects = result;
      this.dataSource = new MatTableDataSource<IProject>(this.projects)
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  filterByKeyword(event: Event) {
    const keyword = (event.target as HTMLInputElement).value;
    this.dataSource.filter = keyword;
  }

  updateProject(elementId: any) {
    console.log(elementId)
  }

  deleteProject(elementId: any) {
    console.log(elementId)
  }
}
