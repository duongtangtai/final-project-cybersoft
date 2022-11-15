import { filter } from 'rxjs';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AppSettings } from 'src/app/app.constants';
import { DialogFormComponent } from 'src/app/share/components/dialog-form/dialog-form.component';
import { DialogNotifyComponent } from 'src/app/share/components/dialog-notify/dialog-notify.component';
import { IStaffModel } from '../../../model/staff.model';
import { ProfileService } from '../../services/profile.service';
import { StaffService } from '../../services/staff.service';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.scss'],
})
export class StaffComponent implements OnInit {
  dataSource: any;
  staffs: any;
  appSettings = AppSettings;

  statusStaff: string = '';

  displayedColumns: string[] = [];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private staffService: StaffService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.getAllStaffs();
    this.initDisplayedColumns();
  }

  initDisplayedColumns() {
    const userRoles = AppSettings.USER_ROLES;
    if (userRoles.includes(AppSettings.ROLE_ADMIN)) {
      //ADMIN
      this.displayedColumns = [
        'firstName',
        'username',
        'email',
        'accountStatus',
        'action',
      ];
    } else {
      //MANAGER
      this.displayedColumns = [
        'firstName',
        'username',
        'email',
        'accountStatus',
      ];
    }
  }

  pagingAndSorting() {
    this.dataSource = new MatTableDataSource<IStaffModel>(this.staffs);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllStaffs() {
    this.statusStaff = '';
    this.staffService.getStaffs().subscribe((content) => {
      this.staffs = content;
      this.pagingAndSorting();
    });
  }

  getAllStaffsWithAccountStatus(status: string) {
    this.statusStaff = status;
    this.staffService.getStaffs().subscribe((content) => {
      this.staffs = content.filter(
        (staff: any) => staff.accountStatus == status
      );
      this.pagingAndSorting();
    });
  }

  filterByKeyword(event: Event) {
    this.dataSource.filter = (event.target as HTMLInputElement).value;
  }

  addStaff() {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_ADD_STAFF,
          type: AppSettings.TYPE_STAFF,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusStaffAfterClosePopup());
  }

  updateStaff(staff: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_UPDATE_STAFF,
          type: AppSettings.TYPE_STAFF,
          element: staff,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusStaffAfterClosePopup());
  }

  deleteStaff(staffId: any) {
    this.dialog
      .open(DialogNotifyComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_DELETE_STAFF,
          message: AppSettings.MESSAGE_DELETE_STAFF,
          id: staffId,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusStaffAfterClosePopup());
  }

  manageRole(staff: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_MANAGE_ROLE,
          type: AppSettings.TYPE_MANAGE_ROLE,
          element: staff,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusStaffAfterClosePopup());
  }

  showStaffDetail(staff: any) {
    this.dialog
      .open(DialogFormComponent, {
        panelClass: 'widthDialogForm',
        data: {
          title: AppSettings.TITLE_STAFF_DETAIL,
          type: AppSettings.TYPE_STAFF,
          element: staff,
        },
      })
      .afterClosed()
      .subscribe(() => this.statusStaffAfterClosePopup());
  }

  statusStaffAfterClosePopup() {
    return this.statusStaff
    ? this.getAllStaffsWithAccountStatus(this.statusStaff)
    : this.getAllStaffs();
  }
}
