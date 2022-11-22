import { filter, delay } from 'rxjs';
import { MyToastrService } from './../../services/my-toastr.service';
import { ITaskModel } from 'src/app/model/task.model';
import { TaskService } from './../../../pages/services/task.service';
import { IStaffModel } from './../../../model/staff.model';
import { StaffService } from './../../../pages/services/staff.service';
import { LocalStorageService } from 'ngx-webstorage';
import { IProjectModel } from './../../../model/project.model';
import { Component, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProjectService } from 'src/app/pages/services/project.service';
import { AppSettings } from '../../../app.constants';
import { DatePipe } from '@angular/common';
import { CommentService } from 'src/app/pages/services/comment.service';
import { ThisReceiver } from '@angular/compiler';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { RoleService } from 'src/app/pages/services/role.service';
import { NotificationService } from 'src/app/pages/services/notification.service';
import { ApexChart, ApexNonAxisChartSeries } from 'ng-apexcharts';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog-form.component.html',
  styleUrls: ['./dialog-form.component.scss'],
})
export class DialogFormComponent implements OnInit {
  //COMMON
  @Input() title: string = '';
  @Input() type: string = 'project | staff | task';
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  appSettings = AppSettings;
  form: any;
  element: any;
  user: any;
  dataSource: any;
  displayedColumns: string[] = [];
  page: string = '';
  //----------PROJECT-------------
  projectData: any;
  projectStatusData: any;
  projectSymbol?: string;
  //task statistics
  taskTotal: number = 0;
  taskStatusToDo: number = 0;
  taskStatusInProgress: number = 0;
  taskStatusDone: number = 0;
  taskChartSeries: ApexNonAxisChartSeries = [];
  taskChartDetails: ApexChart = {
    width: 400,
    type: 'pie',
    toolbar: {
      show: true,
    },
    animations: {
      enabled: true,
      dynamicAnimation: {
        enabled: true
      },
    }
  };
  taskChartColors = ['#0dcaf0', '#0d6efd', '#198754'];
  taskChartLabels: string[] = [];
  //staff statistics
  isStaffStatisticOn = false;
  statisticOfStaff: string = '';
  //--------------------------------
  //--------------STAFF-------------
  staffData: any;
  staffStatusData: any;
  staffGenderData: any;
  //--------------------------------
  //--------------TASK-------------
  taskStatusData: any;
  //--------------------------------
  //------------COMMENT-------------
  commentData: any;
  commentDisplayed: any[] = [];
  respondingToUser: string = '';
  //--------------------------------
  //----------NOTIFICATION----------
  notificationData: any;
  //--------------------------------


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
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
    this.user = this.localStorageService.retrieve(AppSettings.AUTH_DATA);
    this.title = this.data.title;
    this.type = this.data.type;
    this.element = this.data.element;
    this.initForm(this.element);
  }

  initForm(element: any) {
    switch (this.type) {
      case AppSettings.TYPE_PROJECT:
        this.getProjectForm(element);
        break;
      case AppSettings.TYPE_STAFF:
        this.getStaffForm(element);
        break;
      case AppSettings.TYPE_TASK:
        this.getTaskForm(element);
        break;
      case AppSettings.TYPE_COMMENT:
        this.getCommentForm(element);
        break;
      case AppSettings.TYPE_MANAGE_STAFF_IN_PROJECT:
        this.getManageStaffInProjectDialog(element);
        this.form = this.formBuilder.group(''); // we don't need form control
        break;
      case AppSettings.TYPE_MANAGE_ROLE:
        this.getManageRoleInStaffDialog(element);
        break;
      case AppSettings.TYPE_NOTIFICATION:
        this.getNotification(element);
        this.form = this.formBuilder.group('');//we don't need form control
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

  //-----------------FOR MAT TABLE-------------------
  pagingAndSorting(data: any) {
    this.dataSource = new MatTableDataSource<any>(data);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  filterByKeyword(event: Event) {
    this.dataSource.filter = (event.target as HTMLInputElement).value;
  }

  //-----------------PROJECT FORM---------------------

  initProjectForm() {
    //GET ALL PROJECT STATUS
    this.projectService
      .getProjectStatus()
      .subscribe((val) => (this.projectStatusData = val));
    //GET ALL LEADERS
    this.staffService.getStaffsWithLeaderRole().subscribe((val) => {
      this.staffData = val;
    });
    this.form = this.formBuilder.group({
      id: ['', { disabled: true }],
      name: ['', Validators.required],
      description: ['', Validators.required],
      status: ['', Validators.required],
      symbol: ['', Validators.required],
      creatorUsername: [{ value: this.user.userData.username, disabled: true }],
      leaderUsername: ['', Validators.required],
    });
  }

  getProjectForm(project: any) {
    switch (this.title) {
      case AppSettings.TITLE_ADD_PROJECT: //ADD PROJECT
        this.initProjectForm();
        break;
      case AppSettings.TITLE_UPDATE_PROJECT: //UPDATE PROJECT
        this.initProjectForm();
        this.setProjectFormValues(project);
        break;
      case AppSettings.TITLE_PROJECT_DETAIL: //PROJECT DETAIL
        this.initProjectDetail(project);
        break;
      default:
        break;
    }
  }

  setProjectFormValues(projectForm: any) {
    this.form.setValue({
      id: projectForm.id,
      name: projectForm.name,
      description: projectForm.description,
      status: projectForm.status,
      symbol: (this.projectSymbol = projectForm.symbol),
      creatorUsername: projectForm.creator.username,
      leaderUsername: projectForm.leader.username,
    });
  }

  initProjectDetail(project: any) {
    this.projectData = project;
    this.form = this.formBuilder.group({
      name: [{ value: project.name, disabled: true }],
      description: [{ value: project.description, disabled: true }],
      status: [{ value: project.status, disabled: true }],
      creatorUsername: [{ value: project.creator.username, disabled: true }],
      leaderUsername: [{ value: project.leader.username, disabled: true }],
    });
    this.displayedColumns = ['hobbies', 'username', 'email', 'tasks', 'action'];
    //show project information
    this.showProjectInformation()
  }

  showProjectInformation() {
    this.page = AppSettings.TITLE_PROJECT_DETAIL_INFORMATION
    this.isStaffStatisticOn = false;
    //fetch all tasks inside project
    this.taskService
      .getTasksByProject(this.projectData.id)
      .subscribe(content => {
        this.taskTotal = content.length;
        this.taskStatusToDo = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_TODO
        ).length;
        this.taskStatusInProgress = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_IN_PROGRESS
        ).length;
        this.taskStatusDone = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_DONE
        ).length;
        this.taskChartSeries = [
          this.taskStatusToDo,
          this.taskStatusInProgress,
          this.taskStatusDone,
        ];
        this.taskChartLabels = [
          AppSettings.TASK_STATUS_TODO,
          AppSettings.TASK_STATUS_IN_PROGRESS,
          AppSettings.TASK_STATUS_DONE,
        ]
      })
  }

  showProjectStaffList() {
    this.page = AppSettings.TITLE_PROJECT_DETAIL_STAFF_LIST
    //fetch all staffs inside project
    this.staffService
      .getStaffsInsideProjectWithTask(this.projectData.id)
      .subscribe(content => {
        this.staffData = content;
        this.pagingAndSorting(this.staffData);
        console.log("find all inside project with Task")
        console.log(content)
      });
  }

  clearTaskStatistic() {
    this.taskTotal = 0;
    this.taskStatusToDo = 0;
    this.taskStatusInProgress = 0;
    this.taskStatusDone = 0;
    this.taskChartSeries = []
    this.taskChartLabels = [];
  }

  showProjectStaffStatistic(staff: any) {
    this.isStaffStatisticOn = true;
    this.statisticOfStaff = staff.username;
    //fetch all tasks by staffId
    this.clearTaskStatistic()
    this.taskService.getTasksByProjectAndStaff(this.projectData.id, staff.id)
      .subscribe(content => {
        this.taskTotal = content.length;
        this.taskStatusToDo = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_TODO
        ).length;
        this.taskStatusInProgress = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_IN_PROGRESS
        ).length;
        this.taskStatusDone = content.filter(
          (task: any) => task.status == AppSettings.TASK_STATUS_DONE
        ).length;
        this.taskChartSeries = [
          this.taskStatusToDo,
          this.taskStatusInProgress,
          this.taskStatusDone,
        ];
        this.taskChartLabels = [
          AppSettings.TASK_STATUS_TODO,
          AppSettings.TASK_STATUS_IN_PROGRESS,
          AppSettings.TASK_STATUS_DONE,
        ];
      })
    console.log("OK")
  }
  //-------------------END PROJECT FORM--------------------
  //-------------------START STAFF FORM--------------------
  initStaffForm() {
    //initialize account status and genders
    this.staffService
      .getStatus()
      .subscribe((val) => (this.staffStatusData = val));
    this.staffService
      .getGenders()
      .subscribe((val) => (this.staffGenderData = val));
    this.form = this.formBuilder.group({
      id: ['', { disabled: true }],
      username: ['', Validators.required],
      //CREATE NEW STAFF NEED A PASSWORD
      password: [
        '',
        this.title == AppSettings.TITLE_ADD_STAFF
          ? Validators.required
          : { disabled: true },
      ],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      gender: ['', Validators.required],
      avatar: [],
      email: ['', Validators.compose([Validators.email, Validators.required])],
      facebookUrl: [],
      occupation: [],
      department: [],
      hobbies: [],
      accountStatus: ['', Validators.required],
    });
  }

  getStaffForm(staff: IStaffModel) {
    switch (this.title) {
      case AppSettings.TITLE_ADD_STAFF: //ADD STAFF
        this.initStaffForm();
        break;
      case AppSettings.TITLE_UPDATE_STAFF: //UPDATE STAFF
        this.initStaffForm();
        this.setStaffFormValues(staff);
        break;
      case AppSettings.TITLE_STAFF_DETAIL: //STAFF DETAIL
        this.showStaffDetail(staff);
        break;
      default:
        break;
    }
  }

  setStaffFormValues(staffForm: IStaffModel) {
    this.form.setValue({
      id: staffForm.id,
      username: staffForm.username,
      password: staffForm.password, //PASSWORD IS NULL RIGHT NOW
      firstName: staffForm.firstName,
      lastName: staffForm.lastName,
      gender: staffForm.gender,
      avatar: staffForm.avatar,
      email: staffForm.email,
      facebookUrl: staffForm.facebookUrl,
      occupation: staffForm.occupation,
      department: staffForm.department,
      hobbies: staffForm.hobbies,
      accountStatus: staffForm.accountStatus,
    });
  }

  showStaffDetail(staff: any) {
    this.form = this.formBuilder.group({
      id: [{ value: staff.id, disabled: true }],
      username: [{ value: staff.username, disabled: true }],
      firstName: [{ value: staff.firstName, disabled: true }],
      lastName: [{ value: staff.lastName, disabled: true }],
      gender: [{ value: staff.gender, disabled: true }],
      avatar: [{ value: staff.avatar, disabled: true }],
      email: [{ value: staff.email, disabled: true }],
      facebookUrl: [{ value: staff.facebookUrl, disabled: true }],
      occupation: [{ value: staff.occupation, disabled: true }],
      department: [{ value: staff.department, disabled: true }],
      hobbies: [{ value: staff.hobbies, disabled: true }],
      accountStatus: [{ value: staff.accountStatus, disabled: true }],
    });
  }

  //-------------------END STAFF FORM--------------------
  //-------------------START TASK FORM--------------------
  taskProjectNameChange($event: any) {
    //call API to get StaffData with projectId
    const projectId = this.projectData
      .filter((project: any) => project.name == $event)
      .map((project: any) => project.id);
    this.staffService
      .getStaffsInsideProject(projectId)
      .subscribe((val) => (this.staffData = val));
  }

  initTaskForm() {
    //initialize task status
    this.taskService
      .getStatus()
      .subscribe((content) => (this.taskStatusData = content));
    //init projects based on user roles
    if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_MANAGER)) {
      this.projectService
        .getProjects()
        .subscribe((content) => (this.projectData = content));
    } else if (AppSettings.USER_ROLES.includes(AppSettings.ROLE_LEADER)) {
      this.projectService
        .getProjects()
        .subscribe((content: any) => {
          this.projectData = content
            .filter((project: any) => project.leaderUsername == this.user.userData.username)
          console.log(this.projectData)
        });
    }
    this.form = this.formBuilder.group({
      id: ['', { disabled: true }],
      name: ['', Validators.required],
      description: ['', Validators.required],
      startDateExpected: ['', Validators.required],
      endDateExpected: ['', Validators.required],
      startDateInFact: [],
      endDateInFact: [],
      status: ['', Validators.required],
      projectName: ['', Validators.required],
      reporterUsername: [],
    });
  }

  getTaskForm(task: any) {
    switch (this.title) {
      case AppSettings.TITLE_ADD_TASK: //ADD TASK
        this.initTaskForm();
        break;
      case AppSettings.TITLE_UPDATE_TASK: //UPDATE TASK
        this.initTaskForm();
        this.setTaskFormValues(task);
        break;
      case AppSettings.TITLE_TASK_DETAIL: //TASK DETAIL
        this.showTaskDetail(task);
        break;
      default:
        break;
    }
  }

  setTaskFormValues(taskForm: any) {
    this.staffService
      .getStaffsInsideProject(taskForm.project.id)
      .subscribe((val) => (this.staffData = val));
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
      reporterUsername:
        taskForm.reporter != null ? taskForm.reporter.username : null,
    });
  }

  showTaskDetail(task: any) {
    this.form = this.formBuilder.group({
      name: [{ value: task.name, disabled: true }],
      description: [{ value: task.description, disabled: true }],
      startDateExpected: [{ value: task.startDateExpected, disabled: true }],
      endDateExpected: [{ value: task.endDateExpected, disabled: true }],
      startDateInFact: [
        {
          value: task.startDateInFact != null ? task.startDateInFact : ' ',
          disabled: true,
        },
      ],
      endDateInFact: [
        {
          value: task.endDateInFact != null ? task.endDateInFact : ' ',
          disabled: true,
        },
      ],
      status: [{ value: task.status, disabled: true }],
      projectName: [{ value: task.project.name, disabled: true }],
      reporterUsername: [
        {
          value: task.reporter != null ? task.reporter.username : ' ',
          disabled: true,
        },
      ],
    });
  }

  //-------------------END TASK FORM--------------------
  //-------------------START COMMENT FORM--------------------

  initCommentForm() {
    this.commentService
      .getCommentByTaskId(this.element.id)
      .subscribe((content) => {
        this.commentData = content;
        for (let i = 0; i < this.commentData.length; i++) {
          const comment = this.commentData[i];
          let responses = [];
          for (let j = i; j < this.commentData.length; j++) {
            const response = this.commentData[j];
            if (
              response.responseToId != null &&
              response.responseToId == comment.id
            ) {
              responses.push(response);
            }
          }
          this.commentDisplayed.push({
            comment: comment,
            responses: responses,
          });
        }
      });
    this.form = this.formBuilder.group({
      description: ['', Validators.required],
      writerId: ['', Validators.required],
      taskId: ['', Validators.required],
      responseToId: [''],
    });
  }

  getCommentForm(element: ITaskModel) {
    this.initCommentForm();
    this.form.patchValue({
      writerId: this.user.userData.id,
      taskId: element.id,
    });
  }

  responseToComment(comment: any) {
    this.form.patchValue({
      responseToId: comment.id,
    });
    this.respondingToUser = comment.writer.username;
  }

  //-------------------END COMMENT FORM--------------------
  //---------------START MANAGE STAFFS IN PROJECT------------
  getManageStaffInProjectDialog(project: any) {
    this.projectData = project;
    this.displayedColumns = [
      'hobbies',
      'username',
      'email',
      'projects',
      'action',
    ];
    switch (this.title) {
      case AppSettings.TITLE_CURRENT_STAFF:
        this.initRemoveStaffFromProjectTable();
        break;
      case AppSettings.TITLE_AVAILABLE_STAFF:
        this.initAddStaffToProjectTable();
        break;
      default:
        break;
    }
  }

  initRemoveStaffFromProjectTable() {
    //call API to get all the staffs inside this project (with status = ACTIVE)
    this.staffService
      .getStaffsInsideProject(this.projectData.id)
      .subscribe((content) => {
        this.staffData = content;
        this.pagingAndSorting(this.staffData);
      });
  }

  initAddStaffToProjectTable() {
    //call API to get all the staffs outside this project (with status = ACTIVE)
    this.staffService
      .getStaffsOutsideProject(this.projectData.id)
      .subscribe((content) => {
        this.staffData = content;
        this.pagingAndSorting(this.staffData);
      });
  }

  toRemoveStaffFromProjectTable() {
    this.title = AppSettings.TITLE_CURRENT_STAFF;
    this.initRemoveStaffFromProjectTable();
  }

  toAddStaffToProjectTable() {
    this.title = AppSettings.TITLE_AVAILABLE_STAFF;
    this.initAddStaffToProjectTable();
  }

  removeStaffFormProject(staffId: string) {
    this.projectService
      .removeStaffFromProject(this.projectData.id, staffId)
      .subscribe((content) => {
        this.myToastrService.success(content);
        this.initRemoveStaffFromProjectTable();
      });
  }

  addStaffToProject(staffId: string) {
    this.projectService
      .addStaffToProject(this.projectData.id, staffId)
      .subscribe((content) => {
        this.myToastrService.success(content);
        this.initAddStaffToProjectTable();
      });
  }

  //---------------END MANAGE STAFFS IN PROJECT------------
  //--------------START MANAGE ROLES IN STAFF------------
  staff: any = '';
  roleData: any;

  getManageRoleInStaffDialog(staff: any) {
    this.form = this.formBuilder.group({});
    this.staffService.getStaffByIdWithInfo(staff.id).subscribe((content) => {
      this.staff = content;
      let staffRoles: string[] = [];
      for (let i = 0; i < this.staff.roles.length; i++) {
        staffRoles.push(this.staff.roles[i].code);
      }
      this.roleService.getRoles().subscribe((content) => {
        //WHAT IF ROLES CONTAINS ADMIN? MANAGER? LEADER? EMPLOYEE?
        this.roleData = content;
        for (let i = 0; i < this.roleData.length; i++) {
          if (staffRoles.includes(this.roleData[i].code)) {
            this.form.addControl(this.roleData[i].id, new FormControl(true));
          } else {
            this.form.addControl(this.roleData[i].id, new FormControl(false));
          }
        }
        if (staffRoles.includes(AppSettings.ROLE_ADMIN)) {
          //If staff's roles have ADMIN => disable others
          this.checkAllAndDisableOthers(AppSettings.ROLE_ADMIN);
        } else if (staffRoles.includes(AppSettings.ROLE_MANAGER)) {
          //If staff's roles have ADMIN => disable others
          this.checkOneAndDisableOthers(AppSettings.ROLE_MANAGER);
        } else if (staffRoles.includes(AppSettings.ROLE_LEADER)) {
          //If staff's roles have LEADER or EMPLOYEE => disable ADMIN and MANAGER
          this.checkAndDisableAdminAndManager(AppSettings.ROLE_LEADER);
        } else if (staffRoles.includes(AppSettings.ROLE_EMPLOYEE)) {
          this.checkAndDisableAdminAndManager(AppSettings.ROLE_EMPLOYEE);
        }
      });
    });
  }

  change(completed: boolean, roleCode: string) {
    switch (roleCode) {
      case AppSettings.ROLE_ADMIN: //ADMIN => check all roles and disable other roles
        completed ? this.checkAllAndDisableOthers(roleCode) : this.resetAll();
        break;
      case AppSettings.ROLE_MANAGER: //MANAGER, EMPLOYEE => check this role and disable other roles
        completed ? this.checkOneAndDisableOthers(roleCode) : this.resetAll();
        break;
      case AppSettings.ROLE_LEADER: //LEADER => check this role and disable ADMIN & MANAGER
        completed
          ? this.checkAndDisableAdminAndManager(roleCode)
          : this.resetAll();
        break;
      case AppSettings.ROLE_EMPLOYEE:
        completed
          ? this.checkAndDisableAdminAndManager(roleCode)
          : this.resetAll();
        break;
      default:
        break;
    }
  }

  checkAllAndDisableOthers(roleCode: string) {
    //use the role data that we already initialized, then we loop through it and set all
    for (let i = 0; i < this.roleData.length; i++) {
      this.form.setControl(this.roleData[i].id, new FormControl(true)); //CHECK ALL
      if (this.roleData[i].code != roleCode) {
        //DISABLE OTHERS
        this.form.get(this.roleData[i].id).disable();
      }
    }
  }

  checkOneAndDisableOthers(roleCode: string) {
    for (let i = 0; i < this.roleData.length; i++) {
      this.form.setControl(this.roleData[i].id, new FormControl(false)); //uncheck first
      if (this.roleData[i].code == roleCode) {
        //check one
        this.form.setControl(this.roleData[i].id, new FormControl(true));
      } else {
        //disable others
        this.form.get(this.roleData[i].id).disable();
      }
    }
  }

  checkAndDisableAdminAndManager(roleCode: string) {
    for (let i = 0; i < this.roleData.length; i++) {
      if (this.roleData[i].code == roleCode) {
        //if it's LEADER
        this.form.setControl(this.roleData[i].id, new FormControl(true)); // checked
      } else if (
        this.roleData[i].code == AppSettings.ROLE_ADMIN ||
        this.roleData[i].code == AppSettings.ROLE_MANAGER
      ) {
        //if it's ADMIN OR MANAGER
        this.form.get(this.roleData[i].id).disable();
      } //if it's EMPLOYEE, do nothing
    }
  }

  resetAll() {
    for (let i = 0; i < this.roleData.length; i++) {
      this.form.setControl(this.roleData[i].id, new FormControl(false));
      this.form.get(this.roleData[i].id).enable();
    }
  }

  //--------------END MANAGE ROLES IN STAFF------------
  //--------------------START NOTIFICATION------------------

  getNotification(user: any) {
    this.user = user;
    this.initNewNotification()
  }

  initNewNotification() {
    this.title = AppSettings.TITLE_NEW_NOTIFICATION
    this.displayedColumns = ['description', 'createdAt', 'action']
    this.notificationService.getNewNotification(this.user.userData.id).subscribe(content => {
      this.notificationData = content;
      this.pagingAndSorting(content);
    })
  }

  initOldNotification() {
    this.title = AppSettings.TITLE_OLD_NOTIFICATION
    this.notificationService.getOldNotification(this.user.userData.id).subscribe(content => {
      this.notificationData = content;
      this.pagingAndSorting(content);
    })
  }

  deleteNotification(notificationId: string) {
    this.notificationService.deleteById(notificationId).subscribe(content => {
      this.myToastrService.info(content)
      this.title == AppSettings.TITLE_NEW_NOTIFICATION ?
        this.initNewNotification() : this.initOldNotification();
    })
  }


  //--------------------END NOTIFICATION------------------
  //-------------------HANDLE FUNCTIONS--------------------
  convertDateToString(date: any) {
    //convert date to String with pattern ("dd/MM/yyyy") before sending a request to BE
    var datePipe = new DatePipe('en-US');
    return datePipe.transform(date, 'dd/MM/yyyy');
  }

  convertStringToDate(inputPattern: any) {
    //convert string with pattern "dd/MM/yyyy" to date to display
    if (inputPattern != null) {
      var array = inputPattern.split('/');
      var formattedPattern = array[1] + '/' + array[0] + '/' + array[2];
      return new Date(formattedPattern);
    } else {
      return '';
    }
  }

  handleTaskDates() {
    let startDateExpected = this.convertDateToString(
      this.form.get('startDateExpected').value
    );
    let endDateExpected = this.convertDateToString(
      this.form.get('endDateExpected').value
    );
    let startDateInFact = this.convertDateToString(
      this.form.get('startDateInFact').value
    );
    let endDateInFact = this.convertDateToString(
      this.form.get('endDateInFact').value
    );
    this.form.patchValue({
      startDateExpected: startDateExpected,
      endDateExpected: endDateExpected,
      startDateInFact: startDateInFact,
      endDateInFact: endDateInFact,
    });
  }

  succeededAndClose(content: any) {
    this.myToastrService.success(content.toString());
    this.dialogRef.close();
  }

  okFunc($event: any) {
    if (
      this.type == AppSettings.TYPE_MANAGE_STAFF_IN_PROJECT ||
      this.title == AppSettings.TITLE_PROJECT_DETAIL ||
      this.title == AppSettings.TITLE_STAFF_DETAIL ||
      this.title == AppSettings.TITLE_TASK_DETAIL ||
      this.type == AppSettings.TYPE_NOTIFICATION
    ) {
      //JUST CLOSE
      this.dialogRef.close();
    } else if (this.type == AppSettings.TYPE_MANAGE_ROLE) {
      let submitForm: string[] = [];
      Object.keys(this.form.controls).forEach((key) => {
        if (this.form.get(key).value == true) {
          submitForm.push(key);
        }
      });
      this.staffService
        .updateRoles(this.staff.id, submitForm)
        .subscribe((content) => this.succeededAndClose(content));
    } else if (this.form.valid) {
      //NOT MANAGE STAFF IN PROJECT
      if (this.type == AppSettings.TYPE_TASK) {
        this.handleTaskDates();
      }
      const submitForm = this.form.getRawValue();
      switch (this.title) {
        case AppSettings.TITLE_ADD_PROJECT: //ADD PROJECT
          this.projectService
            .saveProject(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_UPDATE_PROJECT: // UPDATE PROJECT
          this.projectService
            .updateProject(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_ADD_STAFF: // ADD STAFF
          this.staffService
            .saveStaff(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_UPDATE_STAFF: // UPDATE STAFF
          this.staffService
            .updateStaff(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_ADD_TASK: // ADD TASK
          this.taskService
            .saveTask(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_UPDATE_TASK: // UPDATE TASK
          this.taskService
            .updateTask(submitForm)
            .subscribe((content) => this.succeededAndClose(content));
          break;
        case AppSettings.TITLE_ADD_COMMENT: // ADD COMMENT
          this.commentService.addComment(submitForm).subscribe((content) => {
            this.myToastrService.success(content);
            this.commentDisplayed = [];
            this.respondingToUser = '';
            this.getCommentForm(this.element);
          });
          break;
        default:
          break;
      }
    }
  }

  afterClose() {
    this.dialogRef.afterClosed();
  }

  cancelFunc($event: any) {
    this.dialogRef.close();
  }
}
