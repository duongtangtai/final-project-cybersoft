import { InjectionToken } from "@angular/core";
import {AppSettings} from "../../app.constants";

const apiUrl = AppSettings.API_ENDPOINT;
export let APP_CONFIG = new InjectionToken("app.config");

export interface PTSAppConfig {
  endpoints: any;
}

export const AppConfig: PTSAppConfig = {
  endpoints: {
    api: `${apiUrl}`,
    auth: {
      login: `${apiUrl}/v1/auth/login/`,
      refreshToken: `${apiUrl}/v1/auth/refresh-token/`,
    },
    project: {
      root: `${apiUrl}/v1/projects/`,
      withInfo: `${apiUrl}/v1/projects/with-info/`,
      getStatus: `${apiUrl}/v1/projects/status/`,
      addUsers: `${apiUrl}/v1/projects/add-users/`,
      removeUsers: `${apiUrl}/v1/projects/remove-users/`,
    },
    staff: {
      root: `${apiUrl}/v1/users/`,
      withInfo: `${apiUrl}/v1/users/with-info/`,
      leaderRole: `${apiUrl}/v1/users/leader-role/`,
      getStatus: `${apiUrl}/v1/users/account-status/`,
      getGenders: `${apiUrl}/v1/users/genders/`,
      insideProject: `${apiUrl}/v1/users/inside-project/`,
      outsideProject: `${apiUrl}/v1/users/outside-project/`,
      updateRoles: `${apiUrl}/v1/users/update-roles/`,
    },
    task: {
      root: `${apiUrl}/v1/tasks/`,
      withInfo: `${apiUrl}/v1/tasks/with-info/`,
      getStatus: `${apiUrl}/v1/tasks/status/`,
      completeTask: `${apiUrl}/v1/tasks/complete-task/`
    },
    profile: {
      root: `${apiUrl}/v1/profiles/`,
      updateProfile: `${apiUrl}/v1/profiles/update-profile/`,
      changePassword: `${apiUrl}/v1/profiles/change-password/`
    },
    file: {
      root: `${apiUrl}/v1/files/`,
    },
    comment: {
      root: `${apiUrl}/v1/comments/`,
      getByTaskId: `${apiUrl}/v1/comments/with-info/by-task/`,
    },
    role: {
      root: `${apiUrl}/v1/roles/`,    
    }
  }
};
