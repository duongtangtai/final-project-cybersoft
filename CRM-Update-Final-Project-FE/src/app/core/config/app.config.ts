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
      login: `${apiUrl}/v1/auth/login`
    },
    project: {
      root: `${apiUrl}/v1/projects/`,
      getStatus: `${apiUrl}/v1/projects/status/`,
    },
    staff: {
      root: `${apiUrl}/v1/users/`,
      getStatus: `${apiUrl}/v1/users/account-status/`,
      getGenders: `${apiUrl}/v1/users/genders/`,
    },
    task: {
      root: `${apiUrl}/v1/tasks/`,
      getStatus: `${apiUrl}/v1/tasks/status/`,
    }
  }
};
