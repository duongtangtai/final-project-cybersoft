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

  constructor(
    readonly authService: AuthService,
    readonly localSt: LocalStorageService
  ) {
  }

  get isLogin() {
    return this.localSt.retrieve(AppConstraint.IS_LOGIN);
  }

  ngOnInit(): void {
    console.log("is login: ", this.isLogin);
  }

}
