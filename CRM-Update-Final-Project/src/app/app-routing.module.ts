import {NgModule} from '@angular/core';
import {PreloadingStrategy, RouterModule, Routes} from '@angular/router';

const routes: Routes = [
    {
        path: 'project',
        loadChildren: () => import('./pages/components/project/project.module').then(m => m.ProjectModule)
    },
    {
        path: 'task',
        loadChildren: () => import('./pages/components/task/task.module').then(m => m.TaskModule)
    },
    {
        path: 'user',
        loadChildren: () => import('./pages/components/user/user.module').then(m => m.UserModule)
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {
        preloadingStrategy: PreloadingStrategy
    })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
