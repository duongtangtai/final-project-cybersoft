import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {AppSettings} from 'src/app/app.constants';
import {DialogFormComponent} from 'src/app/share/components/dialog-form/dialog-form.component';
import {DialogNotifyComponent} from 'src/app/share/components/dialog-notify/dialog-notify.component';
import {IStaffModel} from "../../../model/staff.model";
import {ProfileService} from "../../services/profile.service";
import {StaffService} from "../../services/staff.service";

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
        private staffService: StaffService,
        private dialog: MatDialog
    ) {
    }

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
        this.dataSource.filter = (event.target as HTMLInputElement).value;
    }

    addStaff() {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_ADD_STAFF,
                type: AppSettings.TYPE_STAFF,
            },
        }).afterClosed().subscribe(() => this.getAllStaffs())
    }

    updateStaff(staff: any) {
        this.dialog.open(DialogFormComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_UPDATE_STAFF,
                type: AppSettings.TYPE_STAFF,
                element: staff,
            },
        }).afterClosed().subscribe(() => this.getAllStaffs())
    }

    deleteStaff(staffId: any) {
        this.dialog.open(DialogNotifyComponent, {
            panelClass: 'widthDialogForm',
            data: {
                title: AppSettings.FORM_DELETE_STAFF,
                message: AppSettings.MESSAGE_DELETE_STAFF,
                id: staffId,
            },
        }).afterClosed().subscribe(() => this.getAllStaffs())
    }
}
