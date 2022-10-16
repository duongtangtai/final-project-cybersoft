import { Component, OnInit } from "@angular/core";
import { LocalStorageService } from "ngx-webstorage";
import { AuthService } from "../../core/service/auth.service";

@Component({
  selector: "app-project",
  templateUrl: "./project.component.html",
  styleUrls: ["./project.component.scss"]
})
export class ProjectComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private localSt: LocalStorageService
  ) {
  }

  ngOnInit(): void {
    console.log(this.localSt.retrieve("isLogin"));
  }

}
