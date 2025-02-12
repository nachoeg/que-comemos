import { inject, Injectable, InjectionToken } from '@angular/core';
import { HttpInterceptorFn, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export const AuthInterceptorService: HttpInterceptorFn = (req, next) => { 
    const router = inject(Router);
    // 1. Revisión de la respuesta para almacenar el token
    return next(req).pipe(
        tap(event => {
            if (event instanceof HttpResponse) {
                // Revisa si la respuesta contiene un token
                const token = (event.body as any)?.token; 
                if (token) {
                    console.log('Received token:', token);  // Log para depuración
                    localStorage.setItem('token', token);  // Almacena el token en localStorage
                }
            }
        }),
        catchError((error: HttpErrorResponse) => {
            // 2. Manejo de errores: Si es un error 401, redirigir al login
            if (error.status === 401) {
                console.log('Error 401: No autorizado - Redirigiendo al login');
                //inject(Router).navigate(['/login']);  // Redirigir al login
                router.navigate(['/login']);
            }

            // 3. En caso de otros errores, lanzar nuevamente el error
            return throwError(error);
        })
    );
};


