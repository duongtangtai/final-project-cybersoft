import { BehaviorSubject } from 'rxjs';
export class AppSettings {
    public static API_ENDPOINT = "http://192.168.100.11:8088//rira/api";
    public static LOG_OUT = false;
    public static AUTH_DATA = "auth_data";

    //-----------------------ROLES--------------------------
    public static USER_ROLES: any;
    public static ROLE_ADMIN = "ADM" //ADMIN
    public static ROLE_MANAGER = "MGR" //MANAGER
    public static ROLE_LEADER = "LEAD" //LEADER
    public static ROLE_EMPLOYEE = "EMP" //EMPLOYEE
    //-------------------------------------------------------

    //-------------------------PATHS---------------------------
    public static PATH_LOGIN = "login"
    public static PATH_403 = "403"
    public static PATH_404 = "404";
    public static PATH_500 = "500";
    public static PATH_DASHBOARD = "dashboard"
    public static PATH_STAFF = "staffs"
    public static PATH_PROJECT = "projects"
    public static PATH_TASK = "tasks"
    public static PATH_MY_TASK = "my-tasks"
    public static PATH_PROFILE = "profile"
    public static PATH_PROFILE_CHANGE_PASSWORD = "profile/change-password"
    //-----------------------------------------------------------


    //------------NOTIFICATION'S TITLE----------------
    public static TITLE_SUCCESS = "SUCCESS";
    public static TITLE_ERROR = "ERROR";
    public static TITLE_INFO = "INFO";
    //-----------------------------------------------


    //---------------FOR PROJECT---------------------
    public static TYPE_PROJECT = 'project';
    public static TITLE_ADD_PROJECT = 'Create Project';
    public static TITLE_UPDATE_PROJECT = 'Update Project';
    public static TITLE_DELETE_PROJECT = 'Delete Project';
    public static MESSAGE_DELETE_PROJECT = 'Do you really want to delete this project?'
    //FOR MANAGE STAFFS IN PROJECT
    public static TITLE_CURRENT_STAFF = 'CURRENT STAFFS'
    public static TITLE_AVAILABLE_STAFF = 'AVAILABLE STAFFS'
    public static TYPE_MANAGE_STAFF_IN_PROJECT = 'Manage Staffs'
    //FOR PROJECT DETAILS
    public static TITLE_PROJECT_DETAIL = "PROJECT DETAIL"
    public static TITLE_PROJECT_DETAIL_INFORMATION = "INFORMATION"
    public static TITLE_PROJECT_DETAIL_STAFF_LIST = "STAFF LIST"
    //PROJECT STATUS
    public static PROJECT_STATUS_DOING = "DOING"
    public static PROJECT_STATUS_DONE = "DONE"
    //-----------------------------------------------



    //---------------FOR STAFF---------------------
    public static TYPE_STAFF = 'staff';
    public static TITLE_ADD_STAFF = 'Create Staff';
    public static TITLE_UPDATE_STAFF = 'Update Staff';
    public static TITLE_DELETE_STAFF = 'Delete Staff';
    public static MESSAGE_DELETE_STAFF = 'Do you really want to delete this staff?'
    //FOR MANAGE ROLES IN STAFF
    public static TITLE_MANAGE_ROLE = 'MANAGE ROLES'
    public static TYPE_MANAGE_ROLE = 'MANAGE ROLES'
    ///FOR STAFF DETAILS
    public static TITLE_STAFF_DETAIL = "STAFF DETAIL"
    //STAFF STATUS
    public static STAFF_STATUS_ACTIVE = "ACTIVE"
    public static STAFF_STATUS_TEMPORARILY_BLOCKED = "TEMPORARILY_BLOCKED"
    public static STAFF_STATUS_PERMANENTLY_BLOCKED = "PERMANENTLY_BLOCKED"
    //------------------------------------------------


    //-----------------FOR TASK---------------------
    public static TYPE_TASK = 'task';
    public static TITLE_ADD_TASK = 'Create Task';
    public static TITLE_UPDATE_TASK = 'Update Task';
    public static TITLE_DELETE_TASK = 'Delete Task';
    public static MESSAGE_DELETE_TASK = 'Do you really want to delete this task?'
    public static TITLE_IN_PROGRESS_TASK = 'In Progress Task';
    public static MESSAGE_IN_PROGRESS_TASK = 'Do you really want to work this task?';
    public static TITLE_COMPLETE_TASK = 'Complete Task';
    public static MESSAGE_COMPLETE_TASK = 'Do you really want to complete this task?';
    //FOR TASK DETAILS
    public static TITLE_TASK_DETAIL = "TASK DETAIL"
    //FORM FOR COMMENT
    public static TYPE_COMMENT = "comment";
    public static TITLE_ADD_COMMENT = "Write Comment";
    //TASK STATUS
    public static TASK_STATUS_TODO = "TODO"
    public static TASK_STATUS_IN_PROGRESS = "IN_PROGRESS"
    public static TASK_STATUS_DONE = "DONE"
    //------------------------------------------------


    //-----------------FOR NOTIFICATION---------------------
    public static TYPE_NOTIFICATION = "notification"
    public static TITLE_NEW_NOTIFICATION = "New Notification"
    public static TITLE_OLD_NOTIFICATION = "Old Notification"
    public static NOTIFICATION_EVENT = "NOTIFICATION_EVENT"
    public static LOGOUT_EVENT = "LOGOUT_EVENT"
    //------------------------------------------------------

    //NOTIFY BUTTON
    public static BUTTON_CANCEL = 'CANCEL';
    public static BUTTON_OK = 'OK';

    // DIALOG CONFIG
    public static WITH = '40%'

    //PROFILE MESSAGE
    public static UPDATE_PROFILE_SUCCESSFULLY = "Your profile has been updated!"
    public static UPLOAD_AVATAR_SUCCESSFULLY = "Your avatar has been changed!"
}
