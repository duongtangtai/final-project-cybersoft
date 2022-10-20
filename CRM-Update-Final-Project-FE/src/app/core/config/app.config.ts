import { InjectionToken } from "@angular/core";

const apiUrl = "http://localhost:8088";
export let APP_CONFIG = new InjectionToken("app.config");

export interface PTSAppConfig {
  endpoints: any;
}

export const AppConfig: PTSAppConfig = {
  endpoints: {
    api: `${apiUrl}`,
    auth: {
      login: `${apiUrl}/v1/auth/login`
    }
  }
};
