import { Component, OnInit } from "@angular/core";
import { LocalStorageService } from "ngx-webstorage";
import { AppConstraint } from "../../app.constaint";
import { AuthService } from "../../core/service/auth.service";

@Component({
  selector: "app-main",
  templateUrl: "./main.component.html",
  styleUrls: ["./main.component.scss"]
})
export class MainComponent implements OnInit {

  appConstraint!: AppConstraint;

  constructor(
    readonly authService: AuthService,
    readonly localSt: LocalStorageService
  ) {
  }

  ngOnInit(): void {
  }

}
