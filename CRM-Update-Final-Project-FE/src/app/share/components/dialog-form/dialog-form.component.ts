import { MyToastrService } from './../../services/my-toastr.service';
import { ITaskModel } from 'src/app/model/task.model';
import { TaskService } from './../../../pages/services/task.service';
import { IStaffModel } from './../../../model/staff.model';
import { StaffService } from './../../../pages/services/staff.service';
import { LocalStorageService } from 'ngx-webstorage';
import { IProjectModel } from './../../../model/project.model';
import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { ProjectService } from 'src/app/pages/services/project.service';
import {AppSettings} from "../../../app.constants";
import { DatePipe} from '@angular/common';

@Component({
    selector: 'app-dialog',
    templateUrl: './dialog-form.component.html',
    styleUrls: ['./dialog-form.component.scss'],
})
export class DialogFormComponent implements OnInit {

    @Input() title: string = '';
    @Input() type: string = 'project | staff | task';
    appSettings = AppSettings
    form: any;
    element: any;
    userData: any;
    //PROJECT
    projectData: any;
    projectStatusData: any;
    projectSymbol?: string;
    //STAFF
    staffData: any;
    staffStatusData: any;
    staffGenderData: any;
    //Task
    taskStatusData: any;

    constructor(
        private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<DialogFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private localStorageService: LocalStorageService,
        private projectService: ProjectService,
        private staffService: StaffService,
        private taskService: TaskService,
        private myToastrService: MyToastrService,
    ) {
    }

    ngOnInit(): void {
        this.userData = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
        this.title = this.data.title;
        this.type = this.data.type;
        this.element = this.data.element;
        this.initForm(this.element)
    }

    initForm(element: any) {
        switch (this.type) {
            case AppSettings.TYPE_PROJECT:
                this.getProjectForm(element)
                break;
            case AppSettings.TYPE_STAFF:
                this.getStaffForm(element)
                break;
            case AppSettings.TYPE_TASK:
                this.getTaskForm(element)
                break;
            default:
                break;
        }
    }

    //----------FOR SELECT-OPTIONS---------------------
    compare1(o1: any, o2: any): boolean { 
        return o1.name === o2.name && o1.id === o2.id;
    }

    compare2(o1: any, o2: any): boolean { 
        return o1.name === o2.name && o1.id === o2.id;
    }

    compare3(o1: any, o2: any): boolean { 
        return o1.name === o2.name && o1.id === o2.id;
    }

    //-----------------PROJECT FORM---------------------
    
    initProjectForm() {
        //GET ALL PROJECT STATUS
        this.projectService.getProjectStatus().subscribe(val => this.projectStatusData = val)
        //GET ALL STAFFS
        this.staffService.getStaffs().subscribe(val => {
            this.staffData = val
        })
        this.form = this.formBuilder.group({
            id: ['',{disabled:true}],
            name: ['', Validators.required],
            description: ['', Validators.required],
            status: ['', Validators.required],
            symbol: ['', Validators.required],
            creatorUsername: [{value:this.userData.username,disabled:true}],
            leaderUsername: ['', Validators.required],
        })
    }

    getProjectForm(projectForm: IProjectModel) {
        this.initProjectForm() //ADD PROJECT
        if (this.title == AppSettings.FORM_UPDATE_PROJECT) { //UPDATE PROJECT with OLD INFO
            this.setProjectFormValues(projectForm)
        }
    }

    setProjectFormValues(projectForm: IProjectModel) {
        this.form.setValue({
            id : projectForm.id,
            name : projectForm.name,
            description : projectForm.description,
            status : projectForm.status,
            symbol: this.projectSymbol = projectForm.symbol,
            creatorUsername : projectForm.creatorUsername,
            leaderUsername : projectForm.leaderUsername,
        })
    }
    //-------------------END PROJECT FORM--------------------
    //-------------------START STAFF FORM--------------------
    initStaffForm() { //initialize account status and genders
        this.staffService.getStatus().subscribe(val => this.staffStatusData = val)
        this.staffService.getGenders().subscribe(val => this.staffGenderData = val)
        this.form = this.formBuilder.group({
            id:['',{disabled:true}],
            username:['',Validators.required],
            password:['', Validators.required],
            firstName:['',Validators.required],
            lastName:['',Validators.required],
            gender: ['',Validators.required],
            avatar:[],
            email:['',Validators.compose([Validators.email, Validators.required])],
            facebookUrl:[],
            occupation:[],
            department:[],
            hobbies:[],
            accountStatus:['',Validators.required]
        })
    }

    getStaffForm(staffForm: IStaffModel) {
        this.initStaffForm() //ADD STAFF
        if (this.title == AppSettings.FORM_UPDATE_STAFF) { //UPDATE STAFF with OLD INFO
            this.setStaffFormValues(staffForm)
        }
    }

    setStaffFormValues(staffForm: IStaffModel) {
        this.form.setValue({
            id : staffForm.id,
            username : staffForm.username,
            password : staffForm.password,
            firstName : staffForm.firstName,
            lastName : staffForm.lastName,
            gender : staffForm.gender,
            avatar: staffForm.avatar,
            email : staffForm.email,
            facebookUrl : staffForm.facebookUrl,
            occupation : staffForm.occupation,
            department : staffForm.department,
            hobbies : staffForm.hobbies,
            accountStatus : staffForm.accountStatus
        })
    }

    //-------------------END STAFF FORM--------------------
    //-------------------START TASK FORM--------------------

    initTaskForm() { //initialize all staffs and task status
        this.staffService.getStaffs().subscribe(val => this.staffData = val)
        this.taskService.getStatus().subscribe(val => this.taskStatusData = val)
        this.projectService.getProjects().subscribe(val => this.projectData = val)
        this.form = this.formBuilder.group({
            id: ['',{disabled:true}],
            name: ['',Validators.required],
            description: ['',Validators.required],
            startDateExpected: ['', Validators.required],
            endDateExpected: ['',Validators.required],
            startDateInFact: [],
            endDateInFact: [],
            status: ['',Validators.required],
            projectName: ['',Validators.required],
            reporterUsername: ['',Validators.required],
        })
    }

    getTaskForm(taskForm: ITaskModel) {
        this.initTaskForm() //ADD TASK
        if (this.title == AppSettings.FORM_UPDATE_TASK) { //UPDATE TASK with OLD INFO
            this.setTaskFormValues(taskForm)
        }
    }

    setTaskFormValues(taskForm: ITaskModel) {
        this.form = this.formBuilder.group({
            id: taskForm.id,
            name: taskForm.name,
            description: taskForm.description,
            startDateExpected: this.convertStringToDate(taskForm.startDateExpected),
            endDateExpected: this.convertStringToDate(taskForm.endDateExpected),
            startDateInFact: this.convertStringToDate(taskForm.startDateInFact),
            endDateInFact: this.convertStringToDate(taskForm.endDateInFact),
            status: taskForm.status,
            projectName: taskForm.projectName,
            reporterUsername: taskForm.reporterUsername,
        })
    }

    //-------------------END TASK FORM--------------------
    //-------------------HANDLE FUNCTIONS--------------------
    convertDateToString(date: any) {
        //convert date to String with pattern ("dd/MM/yyyy") before sending a request to BE
        var datePipe = new DatePipe('en-US')
        return datePipe.transform(date, 'dd/MM/yyyy')
    }

    convertStringToDate(inputPattern: any) {
        //convert string with pattern "dd/MM/yyyy" to date to display
        var array = inputPattern.split("/");
        var formattedPattern = array[1] + '/' + array[0] + '/' + array[2];
        return new Date(formattedPattern);
    }

    handleTaskDates() {
        let startDateExpected = this.convertDateToString(this.form.get('startDateExpected').value)
        let endDateExpected = this.convertDateToString(this.form.get('endDateExpected').value)
        let startDateInFact = this.convertDateToString(this.form.get('startDateInFact').value)
        let endDateInFact = this.convertDateToString(this.form.get('endDateInFact').value)
        this.form.patchValue({
            startDateExpected : startDateExpected,
            endDateExpected : endDateExpected,
            startDateInFact : startDateInFact,
            endDateInFact : endDateInFact
        })
    }

    showSuccessfulInfo(content: any) {
        this.myToastrService.success(content.toString())
        this.dialogRef.close()
    }

    okFunc($event: any) {
        if (this.form.valid) {
            if (this.type == AppSettings.TYPE_TASK) {
                this.handleTaskDates()
            }
            const submitForm = this.form.getRawValue()
            switch (this.title) {
                case AppSettings.FORM_ADD_PROJECT: //ADD PROJECT 
                    this.projectService.saveProject(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;
                case AppSettings.FORM_UPDATE_PROJECT: // UPDATE PROJECT
                    this.projectService.updateProject(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;
                case AppSettings.FORM_ADD_STAFF: // ADD STAFF
                    this.staffService.saveStaff(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;
                case AppSettings.FORM_UPDATE_STAFF: // UPDATE STAFF
                    this.staffService.updateStaff(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;
                case AppSettings.FORM_ADD_TASK: // ADD TASK
                    this.taskService.saveTask(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;  
                case AppSettings.FORM_UPDATE_TASK: // UPDATE TASK
                    this.taskService.updateTask(submitForm)
                        .subscribe(content => this.showSuccessfulInfo(content));
                    break;
                default:
                    break;
            }
        }
    }

    afterClose(){
        this.dialogRef.afterClosed()
    }

    cancelFunc($event: any) {
        this.dialogRef.close();
    }
}

