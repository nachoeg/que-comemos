import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap, take } from 'rxjs/operators';
import { LoginServicio } from '../login-servicio/login-servicio';
import { AlertService } from '../alert-service/alert.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private loginServicio: LoginServicio,
    private alertService: AlertService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    return this.loginServicio.isUserLoggedIn$.pipe(
      take(1),
      switchMap((isLoggedIn) => {
        if (!isLoggedIn) {
          console.log(
            'El usuario no está autenticado. Redirigiendo a inicio de sesión.'
          );
          this.alertService.showAlert(
            'Inicie sesión para continuar',
            'warning'
          );
          this.router.navigate(['/iniciar-sesion']);
          return of(false);
        }

        const requiredRoles = route.data['roles'] as string[];
        // Recarga la información del usuario desde localStorage
        this.loginServicio.reloadUserInfoFromLocalStorage();
        return this.loginServicio.userLogged$.pipe(
          take(1),
          map((user) => {
            if (!user) {
              console.log(
                'Usuario no encontrado (no logueado). Redirigiendo a forbidden.'
              );
              this.router.navigate(['/forbidden']);
              return false;
            }

            const userRole = user.rolName;
            console.log('Roles requeridos:', requiredRoles);
            console.log('Rol del usuario:', userRole);

            const hasAccess = requiredRoles.includes(userRole);
            if (!hasAccess) {
              console.log('Usuario sin permisos. Redirigiendo a forbidden.');
              this.router.navigate(['/forbidden']);
              return false;
            }

            console.log('Usuario:', user.nombre, 'tiene acceso:', hasAccess);
            return true;
          })
        );
      }),
      catchError((error) => {
        console.error('Error en la guarda:', error);
        this.router.navigate(['/error']);
        return of(false);
      })
    );
  }
}
