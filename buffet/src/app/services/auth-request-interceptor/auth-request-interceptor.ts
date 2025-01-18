import { Injectable } from '@angular/core';
import { HttpInterceptorFn, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';

export const AuthRequestInterceptor: HttpInterceptorFn = (req, next) => {
    // 2. Agregar el token a las solicitudes salientes
    const token = localStorage.getItem('token');
    
    // Si hay un token, agrega el header Authorization
    if (token) {
        console.log('Sending request with Authorization header: Bearer ' + token);
        const clonedRequest = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(clonedRequest);  // Envía la solicitud clonada con el token
    }

    return next(req);  // Si no hay token, envía la solicitud original sin el token
};