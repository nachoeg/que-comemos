import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, HTTP_INTERCEPTORS, withInterceptors} from '@angular/common/http';
import { AuthInterceptorService } from './services/auth-interceptor-service/auth-interceptor-service';
import { AuthRequestInterceptor } from './services/auth-request-interceptor/auth-request-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(), 
    provideHttpClient(withInterceptors([AuthInterceptorService, AuthRequestInterceptor]))]
};
