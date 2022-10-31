import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {LocalStorageService} from 'ngx-webstorage';
import {AppSettings} from 'src/app/app.constants';
import {IStaffModel} from './../../../model/staff.model';
import {UserModel} from './../../../model/user.model';
import {MyToastrService} from './../../../share/services/my-toastr.service';
import {ProfileService} from './../../services/profile.service';
import {StaffService} from './../../services/staff.service';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

    page = '';
    form: any;
    userAvatarUrl = '';
    userGender = '';
    staffStatusData: any;
    staffGenderData: any;
    //----------UPLOAD AVATAR----------
    isConfirm: boolean = false;
    newAvatarUrl: any;
    newAvatar: any;

    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private staffService: StaffService,
        private profileService: ProfileService,
        private localStorageService: LocalStorageService,
        private myToastrService: MyToastrService,
    ) {
    }

    ngOnInit(): void {
        this.page = this.router.url;
        this.initProfileForm()
        //init user data
        const user = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
        this.userAvatarUrl = user.userData.avatar;
        this.userGender = user.userData.gender;
        console.log(this.userAvatarUrl)
    }

    //---------------END PROFILE FORM-----------------
    //---------------HANDLE FUNCTIONS-----------------

    //-------------START PROFILE FORM-----------------
    initProfileForm() {
        this.form = this.formBuilder.group({
            id: [{value: '', disabled: true}],
            username: [{value: '', disabled: true}],
            password: ['', Validators.required],
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            gender: ['', Validators.required],
            avatar: [{value: '', disabled: true}],
            email: ['', Validators.compose([Validators.email, Validators.required])],
            facebookUrl: [],
            occupation: [],
            department: [],
            hobbies: [],
            accountStatus: [{value: '', disabled: true}]
        })
        this.staffService.getStatus().subscribe(val => this.staffStatusData = val)
        this.staffService.getGenders().subscribe(val => this.staffGenderData = val)
        const userId = this.localStorageService.retrieve(AppSettings.AUTH_DATA).userData.id;
        this.staffService.getStaffById(userId).subscribe(userData => {
            this.setProfileValue(userData)
        })
    }

    setProfileValue(user: IStaffModel) {
        this.form.setValue({ // don't change username, password, and avatar when updating profile
            id: user.id,
            username: user.username,
            password: 'thisIsNothing', // just to make this valid
            firstName: user.firstName,
            lastName: user.lastName,
            gender: user.gender,
            avatar: user.avatar,
            email: user.email,
            facebookUrl: user.facebookUrl,
            occupation: user.occupation,
            department: user.department,
            hobbies: user.hobbies,
            accountStatus: user.accountStatus
        })
    }

    //------FOR SELECT-OPTIONS------
    compare1(o1: any, o2: any): boolean {
        return o1.name === o2.name && o1.id === o2.id;
    }

    //----------UPDATE PROFILE----------
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

    selectFile(event: any) {
        this.isConfirm = true;
        if (!event.target.files[0] || event.target.files[0].length == 0) {
            return;
        }

        let mimeType = event.target.files[0].type;

        if (mimeType.match(/image\/*/) == null) { //HANDLE IF THAT'S NOT AN IMAGE
            return;
        }

        this.newAvatar = event.target.files[0];
        let reader = new FileReader();
        reader.readAsDataURL(event.target.files[0]);

        reader.onload = (_event) => {
            this.newAvatarUrl = reader.result;
            this.profileService.sendData(this.newAvatarUrl);
        }
    }

    uploadAvatar() {
        this.isConfirm = false;
        // request to upload userAvatar
        console.log("upload avatar")
        let userData = this.localStorageService.retrieve(AppSettings.AUTH_DATA).userData;
        const userId = userData.id;
        let submitForm = new FormData()
        submitForm.append("file", this.newAvatar)
        submitForm.append("userId", userId)
        this.profileService.uploadAvatar(submitForm)
            .subscribe(newAvatarUrl => {
                this.storeNewUserAvatar(newAvatarUrl)
                this.myToastrService.success(AppSettings.UPLOAD_AVATAR_SUCCESSFULLY)
            })
    }

    storeNewUserAvatar(newAvatarUrl: any) {
        const oldData = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
        const oldUserData = oldData.userData;
        const newData = new UserModel(
            {
                id: oldUserData.id,
                username: oldUserData.username,
                firstName: oldUserData.firstName,
                lastName: oldUserData.lastName,
                gender: oldUserData.gender,
                avatar: newAvatarUrl,
                email: oldUserData.email,
                facebookUrl: oldUserData.facebookUrl,
                occupation: oldUserData.occupation,
                department: oldUserData.department,
                hobbies: oldUserData.hobbies,
                accountStatus: oldUserData.accountStatus,
            },
            oldData.roleCodes,
            oldData.accessToken,
            oldData.refreshToken
        )
        this.localStorageService.store(AppSettings.AUTH_DATA, newData)
    }
}
