import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AppSettings} from "../../../app.constants";

@Component({
    selector: 'app-dialog-notify',
    templateUrl: './dialog-notify.component.html',
    styleUrls: ['./dialog-notify.component.scss']
})
export class DialogNotifyComponent implements OnInit {

    @Input() type: string = '';
    @Input() message: string = '';

    appSettings = AppSettings;

    constructor(
        public dialogRef: MatDialogRef<DialogNotifyComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
    }

    ngOnInit(): void {
    }

    cancelFunc($event: any) {
        this.dialogRef.close();
    }

    okFunc($event: any) {
        this.dialogRef.close({status: true});
    }
}
