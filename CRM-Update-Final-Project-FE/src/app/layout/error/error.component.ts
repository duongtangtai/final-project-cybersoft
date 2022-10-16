import { Component, OnInit } from "@angular/core";
import { LocalStorageService } from "ngx-webstorage";
import { AppConstraint } from "../../app.constaint";

@Component({
  selector: "app-error",
  templateUrl: "./error.component.html",
  styleUrls: ["./error.component.scss"]
})
export class ErrorComponent implements OnInit {

  constructor(
    private localSt: LocalStorageService
  ) {
  }

  ngOnInit(): void {
    this.localSt.store(AppConstraint.IS_LOGIN, false);
  }

}
