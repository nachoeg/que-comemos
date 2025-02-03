import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from "@angular/router";
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginServicio } from "../login-servicio/login-servicio";
import { catchError, map, Observable, of, switchMap, take } from "rxjs";
@Injectable({
    providedIn: 'root',  // Lo proveemos globalmente para que esté disponible en toda la aplicación
  })
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: LoginServicio) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {

    return this.authService.isUserLoggedIn$.pipe(
      take(1), // Tomar solo el primer valor emitido
      map(isLoggedIn => { 
        if (!isLoggedIn) {
          console.log('El usuario no está autenticado. Redirigiendo a inicio de sesión.');
          this.router.navigate(['/iniciar-sesion']);
          return false;
        }else{          
        // El usuario está autenticado, verificamos el rol
        const requiredRoles = route.data['roles'] as string[];
        const user = this.authService.getUserLoggedIn();
        //const userRole = user.rol?.toString();
        const userRole = user.rolName;

        console.log('Roles requeridos:', requiredRoles);
        console.log('Rol del usuario:', userRole);

        const hasAccess = requiredRoles.includes(userRole);
        if (!hasAccess) {
          console.log('Usuario sin permisos. Redirigiendo a forbidden.');
          this.router.navigate(['/forbidden']); // Redirige a la página de "forbidden"
          return false;
        }

        console.log('Usuario:', user.nombre, 'tiene acceso:', hasAccess);
        return true;
      }
    }),
    catchError(error => {
      console.error('Error en la guarda:', error);
      this.router.navigate(['/error']); // Redirige a una página de error genérica
      return of(false); // Importante: Retorna un Observable<boolean>
    })
  );
}
}
