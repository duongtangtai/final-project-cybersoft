import { ProfileComponent } from './profile.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [];

@NgModule({
  imports: [
    RouterModule.forChild(
        [
            {
                path: '',
                component: ProfileComponent
            },
            {
                path: 'change-password',
                component: ProfileComponent
            },
        ]
    ),
  ],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
