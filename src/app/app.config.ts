import { appHttpInterceptor } from './interceptors/app-http.interceptor';
import {ApplicationConfig} from "@angular/core";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([appHttpInterceptor])
    )
  ]
};
