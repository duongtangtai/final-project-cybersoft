import { filter } from 'rxjs';
import { MyToastrService } from './../../services/my-toastr.service';
import { ITaskModel } from 'src/app/model/task.model';
import { TaskService } from './../../../pages/services/task.service';
import { IStaffModel } from './../../../model/staff.model';
import { StaffService } from './../../../pages/services/staff.service';
import { LocalStorageService } from 'ngx-webstorage';
import { IProjectModel } from './../../../model/project.model';
import {Component, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import { ProjectService } from 'src/app/pages/services/project.service';
import {AppSettings} from "../../../app.constants";
import { DatePipe} from '@angular/common';
import { CommentService } from 'src/app/pages/services/comment.service';
import { ThisReceiver } from '@angular/compiler';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { RoleService } from 'src/app/pages/services/role.service';

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
    user: any;
    //PROJECT
    projectData: any;
    projectStatusData: any;
    projectSymbol?: string;
    //STAFF
    staffData: any;
    staffStatusData: any;
    staffGenderData: any;
    //TASK
    taskStatusData: any;
    //COMMENT
    commentData: any;
    commentDisplayed: any[] = [];
    respondingToUser: string = '';

    constructor(
        private formBuilder: FormBuilder,
        public dialogRef: MatDialogRef<DialogFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private localStorageService: LocalStorageService,
        private projectService: ProjectService,
        private staffService: StaffService,
        private taskService: TaskService,
        private commentService: CommentService,
        private roleService: RoleService,
        private myToastrService: MyToastrService,
    ) {
    }

    ngOnInit(): void {
        this.user = this.localStorageService.retrieve(AppSettings.AUTH_DATA)
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
            case AppSettings.TYPE_COMMENT:
                this.getCommentForm(element)
                break;
            case AppSettings.TYPE_MANAGE_STAFF_IN_PROJECT:
                this.getManageStaffInProjectDialog(element)
                this.form = this.formBuilder.group(""); // we don't need form control
                break;
            case AppSettings.TYPE_MANAGE_ROLE:
                this.getManageRoleInStaffDialog(element)
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
        //GET ALL LEADERS
        this.staffService.getStaffsWithLeaderRole().subscribe(val => {
            this.staffData = val;
        })
        this.form = this.formBuilder.group({
            id: ['',{disabled:true}],
            name: ['', Validators.required],
            description: ['', Validators.required],
            status: ['', Validators.required],
            symbol: ['', Validators.required],
            creatorUsername: [{value:this.user.userData.username,disabled:true}],
            leaderUsername: ['', Validators.required],
        })
    }

    getProjectForm(projectForm: any) {
        console.log(projectForm)
        this.initProjectForm() //ADD PROJECT
        if (this.title == AppSettings.FORM_UPDATE_PROJECT) { //UPDATE PROJECT with OLD INFO
            this.setProjectFormValues(projectForm)
        }
    }

    setProjectFormValues(projectForm: any) {
        this.form.setValue({
            id : projectForm.id,
            name : projectForm.name,
            description : projectForm.description,
            status : projectForm.status,
            symbol: this.projectSymbol = projectForm.symbol,
            creatorUsername : projectForm.creator.username,
            leaderUsername : projectForm.leader.username,
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
            //CREATE NEW STAFF NEED A PASSWORD
            password:['', this.title == AppSettings.FORM_ADD_STAFF ? Validators.required : {disabled:true}],
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
            password : staffForm.password, //PASSWORD IS NULL RIGHT NOW
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
    taskProjectNameChange($event: any) { //call API to get StaffData with projectId
        const projectId = this.projectData.filter((project: any) => project.name == $event).map((project:any) => project.id)
        this.staffService.getStaffsInsideProject(projectId).subscribe(val => this.staffData = val) 
    }

    initTaskForm() { //initialize all staffs and task status
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

    getTaskForm(taskForm: any) {
        this.initTaskForm() //ADD TASK
        if (this.title == AppSettings.FORM_UPDATE_TASK) { //UPDATE TASK with OLD INFO
            this.setTaskFormValues(taskForm)
        }
    }

    setTaskFormValues(taskForm: any) {
        this.staffService.getStaffsInsideProject(taskForm.project.id)
            .subscribe(val => this.staffData = val) 
        this.form = this.formBuilder.group({
            id: taskForm.id,
            name: taskForm.name,
            description: taskForm.description,
            startDateExpected: this.convertStringToDate(taskForm.startDateExpected),
            endDateExpected: this.convertStringToDate(taskForm.endDateExpected),
            startDateInFact: this.convertStringToDate(taskForm.startDateInFact),
            endDateInFact: this.convertStringToDate(taskForm.endDateInFact),
            status: taskForm.status,
            projectName: taskForm.project.name,
            reporterUsername: taskForm.reporter.username,
        })
    }

    //-------------------END TASK FORM--------------------
    //-------------------START COMMENT FORM--------------------

    initCommentForm() {
        this.commentService.getCommentByTaskId(this.element.id)
            .subscribe(content => {
                this.commentData = content
                for (let i = 0 ; i < this.commentData.length ; i++) {
                    const comment = this.commentData[i];
                    let responses = [];
                    for (let j = i ; j < this.commentData.length ; j++) {
                        const response = this.commentData[j];
                        if ( response.responseToId != null && response.responseToId == comment.id) {
                            responses.push(response)
                        }
                    }
                    this.commentDisplayed.push({
                        comment : comment,
                        responses : responses
                    })
                }
            });
        this.form = this.formBuilder.group({
            description: ['', Validators.required],
            writerId: ['',Validators.required],
            taskId: ['',Validators.required],
            responseToId: ['']
        })
    }

    getCommentForm(element: ITaskModel) {
        this.initCommentForm();
        this.form.patchValue({
            writerId : this.user.userData.id,
            taskId : element.id,
        })
    }

    responseToComment(comment: any) {
        this.form.patchValue({
            responseToId: comment.id,
        })
        this.respondingToUser = comment.writer.username;
    }


    //-------------------END COMMENT FORM--------------------
    //---------------START MANAGE STAFFS IN PROJECT------------
    dataSource: any;
    displayedColumns: string[] = ['hobbies', 'username', 'email','projects', 'action'];
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;
    manageStaffTable: string = '';

    pagingAndSorting(data: any) {
        this.dataSource = new MatTableDataSource<IStaffModel>(this.staffData)
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    filterByKeyword(event: Event) {
        this.dataSource.filter = (event.target as HTMLInputElement).value;
    }

    getManageStaffInProjectDialog(project: any) {
        this.projectData = project;
        switch (this.title) {
            case AppSettings.TITLE_CURRENT_STAFF:
                this.initRemoveStaffFromProjectTable()
                break;
            case AppSettings.TITLE_AVAILABLE_STAFF:
                this.initAddStaffToProjectTable()
                break;
            default:
                break;
        }
    }

    initRemoveStaffFromProjectTable() {
        //call API to get all the staffs inside this project (with status = ACTIVE)
        this.staffService.getStaffsInsideProject(this.projectData.id)
            .subscribe(content => {
                this.staffData = content;
                this.pagingAndSorting(this.staffData)
            })
    }

    initAddStaffToProjectTable() {
        //call API to get all the staffs outside this project (with status = ACTIVE)
        this.staffService.getStaffsOutsideProject(this.projectData.id)
            .subscribe(content => {
                this.staffData = content;
                this.pagingAndSorting(this.staffData)
            })
    }

    toRemoveStaffFromProjectTable() {
        this.title = AppSettings.TITLE_CURRENT_STAFF
        this.initRemoveStaffFromProjectTable()
    }

    toAddStaffToProjectTable() {
        this.title = AppSettings.TITLE_AVAILABLE_STAFF
        this.initAddStaffToProjectTable()
    }

    removeStaffFormProject(staffId: string) {
        this.projectService.removeStaffFromProject(this.projectData.id, staffId)
            .subscribe(content => {
                this.myToastrService.success(content)
                this.initRemoveStaffFromProjectTable()
            });
    }

    addStaffToProject(staffId: string) {
        this.projectService.addStaffToProject(this.projectData.id, staffId)
            .subscribe(content => {
                this.myToastrService.success(content)
                this.initAddStaffToProjectTable()
            });
    }

    //---------------END MANAGE STAFFS IN PROJECT------------
    //--------------START MANAGE ROLES IN STAFF------------
    staff: any = '';
    roleData: any;

    getManageRoleInStaffDialog(staff: any) {
        this.form = this.formBuilder.group({})
        this.staffService.getStaffByIdWithInfo(staff.id).subscribe(content => {
            this.staff = content;
            let staffRoles: string[] = [];
            for (let i = 0 ; i < this.staff.roles.length ; i++) {
                staffRoles.push(this.staff.roles[i].code)
            }
            this.roleService.getRoles().subscribe(content => {
                //WHAT IF ROLES CONTAINS ADMIN? MANAGER? LEADER? EMPLOYEE?
                this.roleData = content
                for (let i = 0 ; i < this.roleData.length ; i++) {
                    if (staffRoles.includes(this.roleData[i].code)) {
                        this.form.addControl(this.roleData[i].id, new FormControl(true))
                    } else {
                        this.form.addControl(this.roleData[i].id, new FormControl(false))
                    }
                }
                if (staffRoles.includes('AD')) { //If staff's roles have ADMIN => disable others
                    this.checkAllAndDisableOthers('AD')
                } else if (staffRoles.includes('MGR')) { //If staff's roles have ADMIN => disable others
                    this.checkOneAndDisableOthers('MGR')
                } else if (staffRoles.includes('LEAD')) { //If staff's roles have LEADER or EMPLOYEE => disable ADMIN and MANAGER
                    this.checkAndDisableAdminAndManager('LEAD')
                } else if (staffRoles.includes('EMP')) {
                    this.checkAndDisableAdminAndManager('EMP')
                }
            })
        })
    }

    change(completed: boolean, roleCode: string) {
        switch (roleCode) {
            case 'AD': //ADMIN => check all roles and disable other roles
                completed ? this.checkAllAndDisableOthers(roleCode) : this.resetAll()
                break;
            case 'MGR': //MANAGER, EMPLOYEE => check this role and disable other roles
                completed ? this.checkOneAndDisableOthers(roleCode) : this.resetAll()
                break;
            case 'LEAD': //LEADER => check this role and disable ADMIN & MANAGER
                completed ? this.checkAndDisableAdminAndManager(roleCode) : this.resetAll()
                break;
            case 'EMP':
                completed ? this.checkAndDisableAdminAndManager(roleCode)  : this.resetAll()
                break;
            default:
                break;
        }
    }

    checkAllAndDisableOthers(roleCode: string) {
        //use the role data that we already initialized, then we loop through it and set all
        for (let i = 0 ; i < this.roleData.length ; i++) {
            this.form.setControl(this.roleData[i].id, new FormControl(true)) //CHECK ALL
            if (this.roleData[i].code != roleCode) { //DISABLE OTHERS
                this.form.get(this.roleData[i].id).disable()
            }
        }
    }

    checkOneAndDisableOthers(roleCode: string) {
        for (let i = 0 ; i < this.roleData.length ; i++) {
            this.form.setControl(this.roleData[i].id, new FormControl(false)) //uncheck first
            if (this.roleData[i].code == roleCode) { //check one
                this.form.setControl(this.roleData[i].id, new FormControl(true))
            } else { //disable others
                this.form.get(this.roleData[i].id).disable()
            }
        }
    }

    checkAndDisableAdminAndManager(roleCode: string) {
        for (let i = 0 ; i < this.roleData.length ; i++) {
            if (this.roleData[i].code == roleCode) { //if it's LEADER
                this.form.setControl(this.roleData[i].id, new FormControl(true)) // checked
            } else if (this.roleData[i].code == 'AD' || this.roleData[i].code == 'MGR'){ //if it's ADMIN OR MANAGER
                this.form.get(this.roleData[i].id).disable()
            } //if it's EMPLOYEE, do nothing
        }
    }

    resetAll() {
        for (let i = 0 ; i < this.roleData.length ; i++) {
            this.form.setControl(this.roleData[i].id, new FormControl(false))
            this.form.get(this.roleData[i].id).enable()
        }
    }

    //--------------END MANAGE ROLES IN STAFF------------
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

    succeededAndClose(content: any) {
        this.myToastrService.success(content.toString())
        this.dialogRef.close()
    }

    okFunc($event: any) {
        if (this.type == AppSettings.TYPE_MANAGE_STAFF_IN_PROJECT) { //MANAGE STAFF IN PROJECT
            this.dialogRef.close()
        } else if (this.type == AppSettings.TYPE_MANAGE_ROLE) {
            let submitForm: string[] = [];
            Object.keys(this.form.controls).forEach(key => {
                if (this.form.get(key).value == true) {
                    submitForm.push(key)
                }
            });
            this.staffService.updateRoles(this.staff.id, submitForm)
                .subscribe(content => this.succeededAndClose(content))
        } else if  (this.form.valid) { //NOT MANAGE STAFF IN PROJECT
            if (this.type == AppSettings.TYPE_TASK) {
                this.handleTaskDates()
            }
            const submitForm = this.form.getRawValue()
            switch (this.title) {
                case AppSettings.FORM_ADD_PROJECT: //ADD PROJECT
                    this.projectService.saveProject(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_UPDATE_PROJECT: // UPDATE PROJECT
                    this.projectService.updateProject(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_ADD_STAFF: // ADD STAFF
                    this.staffService.saveStaff(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_UPDATE_STAFF: // UPDATE STAFF
                    this.staffService.updateStaff(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_ADD_TASK: // ADD TASK
                    this.taskService.saveTask(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_UPDATE_TASK: // UPDATE TASK
                    this.taskService.updateTask(submitForm)
                        .subscribe(content => this.succeededAndClose(content));
                    break;
                case AppSettings.FORM_ADD_COMMENT: // ADD COMMENT
                    this.commentService.addComment(submitForm)
                        .subscribe(content => {
                            this.myToastrService.success(content)
                            this.commentDisplayed=[];
                            this.respondingToUser = '';
                            this.getCommentForm(this.element);
                        })
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

