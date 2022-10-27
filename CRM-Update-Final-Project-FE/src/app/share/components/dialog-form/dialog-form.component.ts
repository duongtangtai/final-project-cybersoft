import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AppSettings} from "../../../app.constants";

@Component({
    selector: 'app-dialog',
    templateUrl: './dialog-form.component.html',
    styleUrls: ['./dialog-form.component.scss']
})
export class DialogFormComponent implements OnInit {

    @Input() title: string = '';
    @Input() type: string = 'project | staff | task';

    form = new FormBuilder().group({
        // project
        name: ['', Validators.required],
        description: ['', Validators.required],
        status: ['', Validators.required],
        symbol: ['', Validators.required],
    })

    appSettings = AppSettings;

    constructor(
        private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<DialogFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
    }

    ngOnInit(): void {
        this.title = this.data.title;
        this.type = this.data.type;
    }

    cancelFunc($event: any) {
        this.dialogRef.close();
    }

    okFunc($event: any) {
        this.dialogRef.close({status: true});
    }

}
