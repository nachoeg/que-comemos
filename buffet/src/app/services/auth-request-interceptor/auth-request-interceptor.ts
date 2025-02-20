import { HttpInterceptorFn } from '@angular/common/http';

export const AuthRequestInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    console.log('Sending request with Authorization header: Bearer ' + token);
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });
    return next(clonedRequest);
  }

  return next(req);
};
