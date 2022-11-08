export class AppSettings {
    public static API_ENDPOINT="http://localhost:8088/jira/api";

    public static AUTH_DATA="auth_data";
    // notification's titles
    public static TITLE_SUCCESS = "SUCCESS";
    public static TITLE_ERROR = "ERROR";
    public static TITLE_INFO = "INFO";

    //FORM FOR PROJECT
    public static TITLE_ADD_PROJECT = 'Create Project';
    public static TYPE_PROJECT = 'project';
    public static FORM_ADD_PROJECT = 'Create Project';
    public static FORM_UPDATE_PROJECT = 'Update Project';
    public static FORM_DELETE_PROJECT = 'Delete Project';
    public static MESSAGE_DELETE_PROJECT = 'Do you really want to delete this project?'
    //FORM FOR ADD STAFFS TO PROJECT
    public static TITLE_CURRENT_STAFF = 'CURRENT STAFFS'
    public static TITLE_AVAILABLE_STAFF = 'AVAILABLE STAFFS'
    public static TYPE_MANAGE_STAFF_IN_PROJECT = 'Manage Staffs'

    // FORM FOR STAFF
    public static TYPE_STAFF = 'staff';
    public static FORM_ADD_STAFF = 'Create Staff';
    public static FORM_UPDATE_STAFF = 'Update Staff';
    public static FORM_DELETE_STAFF= 'Delete Staff';
    public static MESSAGE_DELETE_STAFF = 'Do you really want to delete this staff?'
    //FORM FOR ADD ROLES TO STAFF
    public static TITLE_MANAGE_ROLE = 'MANAGE ROLES'
    public static TYPE_MANAGE_ROLE = 'MANAGE ROLES'

    // FORM FOR TASK
    public static TYPE_TASK = 'task';
    public static FORM_ADD_TASK = 'Create Task';
    public static FORM_UPDATE_TASK = 'Update Task';
    public static FORM_DELETE_TASK= 'Delete Task';
    public static MESSAGE_DELETE_TASK = 'Do you really want to delete this task?'
    
    //FORM FOR COMMENT
    public static TYPE_COMMENT = "comment";
    public static FORM_ADD_COMMENT = "Write Comment";

    // NOTIFY BUTTON
    public static BUTTON_CANCEL = 'CANCEL';
    public static BUTTON_OK = 'OK';

    // DIALOG CONFIG
    public static WITH = '40%'

    //PROFILE MESSAGE
    public static UPDATE_PROFILE_SUCCESSFULLY = "Your profile has been updated!"
    public static UPLOAD_AVATAR_SUCCESSFULLY = "Your avatar has been changed!"
}