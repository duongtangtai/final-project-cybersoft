import { AppSettings } from 'src/app/app.constants';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { PermissionGuard } from './core/guards/permission.guard';
import { ForbiddenComponent } from './layouts/error/forbidden/forbidden.component';
import { NotFoundComponent } from './layouts/error/not-found/not-found.component';
import { ServerErrorComponent } from './layouts/error/server-error/server-error.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: AppSettings.PATH_LOGIN,
          loadChildren: () =>
            import('../app/pages/components/login/login.module').then(
              (m) => m.LoginModule
            ),
        },
        {
          path: AppSettings.PATH_DASHBOARD,
          loadChildren: () =>
            import('../app/pages/components/dashboard/dashboard.module').then(
              (m) => m.DashboardModule
            ),
        },
        {
          path: AppSettings.PATH_PROJECT,
          loadChildren: () =>
            import('../app/pages/components/project/project.module').then(
              (m) => m.ProjectModule
            ),
          canActivate: [AuthGuard, PermissionGuard],
          data: {
            requiredRoles: [AppSettings.ROLE_MANAGER, AppSettings.ROLE_LEADER],
          },
        },
        {
          path: AppSettings.PATH_TASK,
          loadChildren: () =>
            import('../app/pages/components/task/task.module').then(
              (m) => m.TaskModule
            ),
          canActivate: [AuthGuard, PermissionGuard],
          data: {
            requiredRoles: [AppSettings.ROLE_MANAGER, AppSettings.ROLE_LEADER],
          },
        },
        {
          path: AppSettings.PATH_MY_TASK,
          loadChildren: () =>
            import('../app/pages/components/task/task.module').then(
              (m) => m.TaskModule
            ),
          canActivate: [AuthGuard, PermissionGuard],
          data: {
            requiredRoles: [AppSettings.ROLE_EMPLOYEE], //only for employees
          },
        },
        {
          path: AppSettings.PATH_STAFF,
          loadChildren: () =>
            import('../app/pages/components/staff/staff.module').then(
              (m) => m.StaffModule
            ),
          canActivate: [AuthGuard, PermissionGuard],
          data: {
            requiredRoles: [AppSettings.ROLE_MANAGER],
          },
        },
        {
          path: AppSettings.PATH_PROFILE,
          loadChildren: () =>
            import('../app/pages/components/profile/profile.module').then(
              (m) => m.ProfileModule
            ),
          canActivate: [AuthGuard, PermissionGuard],
          data: {
            requiredRoles: [
              AppSettings.ROLE_MANAGER,
              AppSettings.ROLE_LEADER,
              AppSettings.ROLE_EMPLOYEE,
            ],
          },
        },
        {
          path: '403',
          component: ForbiddenComponent,
        },
        {
          path: '404',
          component: NotFoundComponent,
        },
        {
          path: '500',
          component: ServerErrorComponent,
        },
        {
          path: '',
          redirectTo: 'login',
          pathMatch: 'full',
        },
        {
          path: '**',
          component: NotFoundComponent,
        },
      ]
      // {enableTracing: true}
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
