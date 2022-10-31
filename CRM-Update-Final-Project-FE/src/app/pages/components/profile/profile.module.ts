import { ProfileComponent } from './profile.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common'
import { ProfileRoutingModule } from './profile-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/share/modules/material-module';


@NgModule({
  imports: [
    CommonModule, ReactiveFormsModule, FormsModule,
    ProfileRoutingModule,
    MaterialModule,
  ],
  declarations: [ProfileComponent],
})
export class ProfileModule { 
  
}
