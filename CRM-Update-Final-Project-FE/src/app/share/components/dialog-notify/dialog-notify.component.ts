import { MyToastrService } from './../../services/my-toastr.service';
import { StaffService } from './../../../pages/services/staff.service';
import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { ProjectService } from 'src/app/pages/services/project.service';
import {AppSettings} from "../../../app.constants";
import { TaskService } from 'src/app/pages/services/task.service';

@Component({
    selector: 'app-dialog-notify',
    templateUrl: './dialog-notify.component.html',
    styleUrls: ['./dialog-notify.component.scss']
})
export class DialogNotifyComponent implements OnInit {

    @Input() title: string = '';
    @Input() message: string = '';
    @Input() id: string = '';

    appSettings = AppSettings;

    constructor(
        public dialogRef: MatDialogRef<DialogNotifyComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private projectService: ProjectService,
        private staffService: StaffService,
        private taskService: TaskService,
        private myToastrService: MyToastrService,
    ) {
    }

    ngOnInit(): void {
        this.title = this.data.title;
        this.message = this.data.message;
        this.id = this.data.id;
    }

    cancelFunc($event: any) {
        this.dialogRef.close();
    }

    okFunc($event: any) { //DELETE BASED ON THE TITLE
        switch (this.title) {
            case AppSettings.FORM_DELETE_PROJECT:
                this.projectService.deleteProject(this.id)
                    .subscribe(content => this.myToastrService.success(content.toString()));
                break;
            case AppSettings.FORM_DELETE_STAFF:
                this.staffService.deleteStaff(this.id)
                    .subscribe(content => this.myToastrService.success(content.toString()));
                break;
            case AppSettings.FORM_DELETE_TASK:
                this.taskService.deleteTask(this.id)
                    .subscribe(content => this.myToastrService.success(content.toString()));
                break;
            default:
                break;
        }
        this.dialogRef.close({status: true});
    }
}
