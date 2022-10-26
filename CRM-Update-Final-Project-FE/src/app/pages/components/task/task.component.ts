import { ITaskModel } from 'src/app/model/task.model';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit {
  dataSource: any;
  tasks: any;
  taskStatus: string[] = ['UNASSIGNED', 'STARTED', 'COMPLETED'];
  
  displayedColumns: string[] = ['name', 'description', 'status', 'action'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(
      private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.getAllStaffs(); 
  }

  pagingAndSorting() {
    this.dataSource = new MatTableDataSource<ITaskModel>(this.tasks)
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllStaffs() {
    this.taskService.getTasks().subscribe(result => {
      this.tasks = result;
      this.pagingAndSorting()
    });
  }

  getAllTasksWithStatus(status: string) {
    this.taskService.getTasks().subscribe(result => {
      this.tasks = result;
      switch (status) {
        case this.taskStatus[0]:
          this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[0])
          break;
        case this.taskStatus[1]:
          this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[1])
          break;
        case this.taskStatus[2]:
          this.tasks = this.tasks.filter((element: any) => element.status == this.taskStatus[2])
          break;
      }
      this.pagingAndSorting()
    });
  }

  filterByKeyword(event: Event) {
    const keyword = (event.target as HTMLInputElement).value;
    this.dataSource.filter = keyword;
  }

  updateTask(taskId: any) {
    console.log("update task with ID: " + taskId)
  }

  deleteTask(taskId: any) {
    console.log("delete task with ID: " + taskId)
  }
}

