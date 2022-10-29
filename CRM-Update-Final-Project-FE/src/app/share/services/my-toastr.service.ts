import { Injectable } from "@angular/core";
import { ToastrService } from "ngx-toastr";
import { AppSettings } from "src/app/app.constants";

@Injectable({
    providedIn: 'root'
})
export class MyToastrService {
    constructor(
        private toastrService: ToastrService
    )
    {}

    public success(message: string) {
        this.toastrService.success(message, AppSettings.TITLE_SUCCESS, {
            //config...
        })

    }

    error(message: string) {
        this.toastrService.error(message, AppSettings.TITLE_ERROR, {
            //config...
        })
    }

    info(message: string) {
        this.toastrService.info(message, AppSettings.TITLE_INFO, {
            //config...
        })
    }
}