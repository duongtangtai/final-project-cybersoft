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
        switch (this.title) {
            case AppSettings.FORM_ADD_PROJECT:
                this.getAddProjectForm()
                break;
            case AppSettings.FORM_UPDATE_PROJECT:
                this.getUpdateProjectForm(element)
                break;
            case AppSettings.FORM_ADD_STAFF:
                    this.getAddStaffForm()
                break;
            case AppSettings.FORM_UPDATE_STAFF:
                    this.getUpdateStaffForm(element)
                break;
            case AppSettings.FORM_ADD_TASK:
                    this.getAddTaskForm()
                break;
            case AppSettings.FORM_UPDATE_TASK:
                    this.getUpdateTaskForm(element)
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
    }

    getAddProjectForm() {
        this.initProjectForm()
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

    getUpdateProjectForm(projectForm: IProjectModel) {
        this.initProjectForm()
        this.form = this.formBuilder.group({
            id: [projectForm.id,{disabled:true}],
            name: [projectForm.name, Validators.required],
            description: [projectForm.description, Validators.required],
            status: [projectForm.status, Validators.required],
            symbol: [this.projectSymbol = projectForm.symbol,Validators.required],
            creatorUsername: [{value:projectForm.creatorUsername,disabled:true}],
            leaderUsername: [projectForm.leaderUsername, Validators.required],
        })
    }

    //-------------------END PROJECT FORM--------------------
    //-------------------START STAFF FORM--------------------
    initStaffForm() { //initialize account status and genders
        this.staffService.getStatus().subscribe(val => this.staffStatusData = val)
        this.staffService.getGenders().subscribe(val => this.staffGenderData = val)
    }

    getAddStaffForm() {
        this.initStaffForm()
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

    getUpdateStaffForm(staffForm: IStaffModel) {
        this.initStaffForm();
        this.form = this.formBuilder.group({
            id:[staffForm.id,{disabled:true}],
            username:[staffForm.username,Validators.required],
            password:[staffForm.password, Validators.required],
            firstName:[staffForm.firstName,Validators.required],
            lastName:[staffForm.lastName,Validators.required],
            gender: [staffForm.gender,Validators.required],
            avatar:[],
            email:[staffForm.email,Validators.compose([Validators.email, Validators.required])],
            facebookUrl:[staffForm.facebookUrl],
            occupation:[staffForm.occupation],
            department:[staffForm.department],
            hobbies:[staffForm.hobbies],
            accountStatus:[staffForm.accountStatus,Validators.required]
        })
    }

    //-------------------END STAFF FORM--------------------
    //-------------------START TASK FORM--------------------

    initTaskForm() { //initialize all staffs and task status
        this.staffService.getStaffs().subscribe(val => this.staffData = val)
        this.taskService.getStatus().subscribe(val => this.taskStatusData = val)
        this.projectService.getProjects().subscribe(val => this.projectData = val)
    }

    getAddTaskForm() {
        this.initTaskForm();
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

    getUpdateTaskForm(task: ITaskModel) {
        this.initTaskForm();
        this.form = this.formBuilder.group({
            id: [task.id,{disabled:true}],
            name: [task.name,Validators.required],
            description: [task.description,Validators.required],
            startDateExpected: [task.startDateExpected, Validators.required],
            endDateExpected: [task.endDateExpected,Validators.required],
            startDateInFact: [task.startDateInFact],
            endDateInFact: [task.endDateInFact],
            status: [task.status,Validators.required],
            projectName: [{value:task.projectName,disabled:true}],
            reporterUsername: [task.reporterUsername,Validators.required],
        })
    }
    //-------------------END TASK FORM--------------------




    //-------------------HANDLE FUNCTIONS--------------------
    cancelFunc($event: any) {
        this.dialogRef.close();
    }

    transformDate(date: any) {
        var datePipe = new DatePipe('en-US')
        return datePipe.transform(date, 'dd/MM/yyyy')
    }

    transformTaskDate() {
        let startDateExpected = this.transformDate(this.form.get('startDateExpected').value)
        this.form.get('startDateExpected').patchValue(startDateExpected)
        let endDateExpected = this.transformDate(this.form.get('endDateExpected').value)
        this.form.get('endDateExpected').patchValue(endDateExpected)
        let startDateInFact = this.transformDate(this.form.get('startDateInFact').value)
        this.form.get('startDateInFact').patchValue(startDateInFact)
        let endDateInFact = this.transformDate(this.form.get('endDateInFact').value)
        this.form.get('endDateInFact').patchValue(endDateInFact)
    }

    okFunc($event: any) {
        if (this.form.valid) {
            if (this.type == AppSettings.TYPE_TASK) {
                this.transformTaskDate()
            }
            const submitForm = this.form.getRawValue()
            switch (this.title) {
                case AppSettings.FORM_ADD_PROJECT: //ADD PROJECT 
                    this.projectService.saveProject(submitForm)
                    break;
                case AppSettings.FORM_UPDATE_PROJECT: // UPDATE PROJECT
                    this.projectService.updateProject(submitForm)
                    break;
                case AppSettings.FORM_ADD_STAFF: // ADD STAFF
                    this.staffService.saveStaff(submitForm)
                    break;
                case AppSettings.FORM_UPDATE_STAFF: // UPDATE STAFF
                    this.staffService.updateStaff(submitForm)
                    break;
                case AppSettings.FORM_ADD_TASK: // ADD TASK
                    this.taskService.saveTask(submitForm)
                    break;  
                case AppSettings.FORM_UPDATE_TASK: // UPDATE TASK
                    this.taskService.updateTask(submitForm)
                    break;
                default:
                    break;
            }
        }
    }

    afterClose(){
        this.dialogRef.afterClosed
    }
}
