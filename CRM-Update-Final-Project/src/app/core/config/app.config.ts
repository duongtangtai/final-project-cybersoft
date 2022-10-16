import {InjectionToken} from '@angular/core';

const apiUrl = 'http://localhost:8081';
export let APP_CONFIG = new InjectionToken('./app.config');

export interface PTSAppConfig {
    endpoints: any;
}

export const AppConfig: PTSAppConfig = {
    endpoints: {
        api: `${apiUrl}`,
        auth: {
            login: `${apiUrl}/api/auth/login`,
            validateToken: `${apiUrl}/api/auth/validate-token`,
        }
    },
};
