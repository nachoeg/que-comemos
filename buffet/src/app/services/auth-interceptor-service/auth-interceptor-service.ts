import { inject, Injectable, InjectionToken } from '@angular/core';
import {
  HttpInterceptorFn,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert-service/alert.service';

export const AuthInterceptorService: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const alertService = inject(AlertService);

  return next(req).pipe(
    tap((event) => {
      if (event instanceof HttpResponse) {
        const token = (event.body as any)?.token;
        if (token) {
          console.log('Received token:', token);
          localStorage.setItem('token', token);
        }
      }
    }),
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && error.error === 'Token expirado') {
        console.log('Error 401: Token expirado - Redirigiendo al login');
        alertService.showAlert(
          'Su sesión ha expirado, inicie sesión nuevamente.',
          'danger'
        );
      } else if (error.status === 401) {
        console.log('Error 401: No autorizado - Redirigiendo al login');
        alertService.showAlert(
          'No autorizado, inicie sesión nuevamente.',
          'danger'
        );
      } else if (error.status === 403) {
        console.log('Error 403: Prohibido - Redirigiendo al login');
        alertService.showAlert(
          'Prohibido, inicie sesión nuevamente.',
          'danger'
        );
      } else if (error.status === 0) {
        console.log('Error 0: Problema de red o CORS');
        alertService.showAlert('Problema de red o CORS.', 'danger');
        // Manejar el error de red o CORS aquí si es necesario
      }
      router.navigate(['/iniciar-sesion']);
      console.log('El error es: ', error);
      return throwError(error);
    })
  );
};
