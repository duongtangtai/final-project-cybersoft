import { IUserModel, UserModel } from './../../../model/user.model';
import { MyToastrService } from './../../../share/services/my-toastr.service';
import { ProfileService } from './../../services/profile.service';
import { IStaffModel } from './../../../model/staff.model';
import { LocalStorageService } from 'ngx-webstorage';
import { StaffService } from './../../services/staff.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppSettings } from 'src/app/app.constants';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  page = '';
  form: any;
  userData: any;
  staffStatusData: any;
  staffGenderData: any;


  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private staffService: StaffService,
    private profileService: ProfileService,
    private localStorageService: LocalStorageService,
    private myToastrService: MyToastrService,
  ) {}

  ngOnInit(): void {
    this.page = this.router.url;
    this.initProfileForm()
  }

  //-------------START PROFILE FORM-----------------
  initProfileForm() {
    this.form = this.formBuilder.group({
      id:[{value:'',disabled:true}],
      username:[{value:'',disabled:true}],
      password:['', Validators.required],
      firstName:['',Validators.required],
      lastName:['',Validators.required],
      gender: ['',Validators.required],
      avatar:[{value:'',disabled:true}],
      email:['',Validators.compose([Validators.email, Validators.required])],
      facebookUrl:[],
      occupation:[],
      department:[],
      hobbies:[],
      accountStatus:[{value:'',disabled:true}]
  })
    this.staffService.getStatus().subscribe(val => this.staffStatusData = val)
    this.staffService.getGenders().subscribe(val => this.staffGenderData = val)
    const userId = this.localStorageService.retrieve(AppSettings.AUTH_DATA).userData.id;
    this.staffService.getStaffById(userId).subscribe(val => {
      this.userData = val;
      this.setProfileValue(this.userData)
    })
  }
  
  setProfileValue(user: IStaffModel) {
    this.form.setValue({ // don't change username, password, and avatar when updating profile
      id : user.id,
      username : user.username,
      password : 'thisIsNothing', // just to make this valid
      firstName : user.firstName,
      lastName : user.lastName,
      gender : user.gender,
      avatar: user.avatar,
      email : user.email,
      facebookUrl : user.facebookUrl,
      occupation : user.occupation,
      department : user.department,
      hobbies : user.hobbies,
      accountStatus : user.accountStatus
    })
  }

  //---------------END PROFILE FORM-----------------
  //---------------HANDLE FUNCTIONS-----------------

  //------FOR SELECT-OPTIONS------
  compare1(o1: any, o2: any): boolean { 
    return o1.name === o2.name && o1.id === o2.id;
  }

  //------UPDATE PROFILE----------
  updateProfile() {
    const submitForm = this.form.getRawValue();
    console.log(submitForm);
    this.profileService.updateProfile(submitForm)
        .subscribe(content => {
          this.storeNewUserProfile(content)
          this.myToastrService.success(AppSettings.UPDATE_PROFILE_SUCCESSFULLY)
        })
  }
  storeNewUserProfile(userData: any) {
    const oldData = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
    const newData = new UserModel(
      userData.userData,
      userData.roleCodes,
      oldData.accessToken,
      oldData.refreshToken
    )
    this.localStorageService.store(AppSettings.AUTH_DATA, newData)
  }
}
