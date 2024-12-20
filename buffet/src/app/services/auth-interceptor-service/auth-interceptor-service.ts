import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {
    constructor() { }

    //   intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    //     const token: string = localStorage.getItem('token') as string;;

    //     let request = req;

    //     if (token) {
    //       request = req.clone({
    //         setHeaders: {
    //           authorization: `Bearer ${ token }`
    //         }
    //       });
    //     }
    //     if (token) {
    //         const authReq = request.clone({
    //           headers: request.headers.set('Authorization', `Bearer ${token}`)
    //         });
    //         return next.handle(authReq);
    //       }

    //     return next.handle(request);
    //   }
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            tap(event => {
                if (event instanceof HttpResponse) {
                    const token = event.headers.get('token');
                    console.log('Received token:', token); // Log the token for debugging
                    if (token) {
                        localStorage.setItem('token', token);
                        console.log('Token stored in localStorage');
                    }
                    // Log the entire response headers

                    const headers = event.headers;

                    for (const headerName in headers) {
                        if (headers.hasOwnProperty(headerName)) {
                            const headerValue = headers.get(headerName);
                            console.log(`${headerName}: ${headerValue}`);
                        }
                    }

                }
            })
        );
    }
}
