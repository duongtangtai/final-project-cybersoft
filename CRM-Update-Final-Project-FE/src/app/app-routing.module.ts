import { NgModule } from "@angular/core";
import { PreloadingStrategy, RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./core/guard/auth.guard";
import { ErrorComponent } from "./layout/error/error.component";

const routes: Routes = [
    {
        path: "account",
        loadChildren: () => import("./pages/account/account.module").then(m => m.AccountModule)
    },
    {
        path: "project",
        canActivate: [AuthGuard],
        loadChildren: () => import("./pages/project/project.module").then(m => m.ProjectModule)
    },
    {
        path: "task",
        canActivate: [AuthGuard],
        loadChildren: () => import("./pages/task/task.module").then(m => m.TaskModule)
    },
    {
        path: "user",
        canActivate: [AuthGuard],
        loadChildren: () => import("./pages/user/user.module").then(m => m.UserModule)
    },
    {
        path: "",
        redirectTo: "account/login",
        pathMatch: "full"
    },
    {
        path: "**",
        component: ErrorComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {
        preloadingStrategy: PreloadingStrategy
    })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
