import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class CommonService {

    constructor() {
    }

    resetValue(values: boolean[]) {
        values.forEach(value => value = false);
    }
}
