import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "./core/guards/auth.guard";
import {ErrorComponent} from "./layouts/error/error.component";

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: 'login',
                    loadChildren: () => import('../app/pages/components/login/login.module').then(m => m.LoginModule),
                },
                {
                    path: 'project',
                    loadChildren: () => import('../app/pages/components/project/project.module').then(m => m.ProjectModule),
                    canActivate: [AuthGuard]
                }
                ,
                {
                    path: 'task',
                    loadChildren: () => import('../app/pages/components/task/task.module').then(m => m.TaskModule),
                    canActivate: [AuthGuard]
                },
                {
                    path: 'staff',
                    loadChildren: () => import('../app/pages/components/staff/staff.module').then(m => m.StaffModule),
                    canActivate: [AuthGuard]
                },
                {
                    path: 'profile',
                    loadChildren: () => import('../app/pages/components/profile/profile.module').then(m => m.ProfileModule),
                    canActivate: [AuthGuard]
                },
                {
                    path: '',
                    redirectTo: 'login',
                    pathMatch: 'full'
                },
                {
                    path: '**',
                    component: ErrorComponent
                },
            ],
            // {enableTracing: true}
        ),
    ],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
