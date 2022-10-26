import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'app-dialog',
    templateUrl: './dialog.component.html',
    styleUrls: ['./dialog.component.scss']
})
export class DialogComponent implements OnInit {

    @Input() title: string = '';
    @Input() type: string = 'project | staff | task';

    form = new FormBuilder().group({
        // project
        name: ['', Validators.required],
        description: ['', Validators.required],
        status: ['', Validators.required],
        symbol: ['', Validators.required],
    })

    constructor(
        private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<DialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
    }

    ngOnInit(): void {
        this.title = this.data.title;
        this.type = this.data.type;
    }

    close() {
        this.dialogRef.close();
    }

    submit() {
        // code something here
    }
}
