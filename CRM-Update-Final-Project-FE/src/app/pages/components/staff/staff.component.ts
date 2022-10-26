import { filter } from 'rxjs';
import { IStaffModel } from './../../../model/staff.model';
import { StaffService } from './../../services/staff.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.scss']
})
export class StaffComponent implements OnInit {
  dataSource: any;
  staffs: any;
  accountStatus: string[] = ['ACTIVE', 'TEMPORARILY_BLOCKED', 'PERMANENTLY_BLOCKED'];
  
  displayedColumns: string[] = ['avatar', 'firstName', 'email', 'accountStatus', 'action'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(
      private staffService: StaffService
  ) {}

  ngOnInit(): void {
    this.getAllStaffs(); 
  }

  pagingAndSorting() {
    this.dataSource = new MatTableDataSource<IStaffModel>(this.staffs)
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllStaffs() {
    this.staffService.getStaffs().subscribe(result => {
      this.staffs = result;
      this.pagingAndSorting()
    });
  }

  getAllStaffsWithAccountStatus(status: string) {
    this.staffService.getStaffs().subscribe(result => {
      this.staffs = result;
      switch (status) {
        case this.accountStatus[0]:
          this.staffs = this.staffs.filter((element: any) => element.accountStatus == this.accountStatus[0])
          break;
        case this.accountStatus[1]:
          this.staffs = this.staffs.filter((element: any) => element.accountStatus == this.accountStatus[1])
          break;
        case this.accountStatus[2]:
          this.staffs = this.staffs.filter((element: any) => element.accountStatus == this.accountStatus[2])
          break;
      }
      this.pagingAndSorting()
    });
  }

  filterByKeyword(event: Event) {
    const keyword = (event.target as HTMLInputElement).value;
    this.dataSource.filter = keyword;
  }

  updateStaff(staffId: any) {
    console.log("update staff with ID: " + staffId)
  }

  deleteStaff(staffId: any) {
    console.log("delete staff with ID: " + staffId)
  }
}
