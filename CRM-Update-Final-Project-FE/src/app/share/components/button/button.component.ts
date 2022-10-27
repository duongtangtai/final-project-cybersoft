import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AppSettings} from "../../../app.constants";

@Component({
    selector: 'app-button',
    templateUrl: './button.component.html',
    styleUrls: ['./button.component.scss']
})
export class ButtonComponent {

    @Input() label: string = '';
    @Output() onClick = new EventEmitter<any>();

    appSettings = AppSettings;

    onClickButton($event: MouseEvent) {
        this.onClick.emit(event);
    }
}
