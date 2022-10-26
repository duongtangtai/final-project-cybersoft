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
      getAll: `${apiUrl}/v1/projects`
    },
    staff: {
      getAll: `${apiUrl}/v1/users`
    },
    task: {
      getAll: `${apiUrl}/v1/tasks`
    }
  }
};
