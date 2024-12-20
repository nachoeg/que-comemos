import { Injectable } from '@angular/core';
import { HttpInterceptorFn, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export const AuthInterceptorService: HttpInterceptorFn = (req, next) => { 
    return next(req).pipe(
            tap(event => {
                if (event instanceof HttpResponse) {
                    const token = (event.body as any).token; 
                    
                    console.log('Received token:', token); // Log the token for debugging
                    if (token) {
                        localStorage.setItem('token', token);
                        console.log('Token stored in localStorage');
                    }

                }
            })
        );
    }

