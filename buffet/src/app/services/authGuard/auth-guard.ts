import { CanActivate } from "@angular/router";
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginServicio } from "../login-servicio/login-servicio";
@Injectable({
    providedIn: 'root',  // Lo proveemos globalmente para que esté disponible en toda la aplicación
  })
export class AuthGuard implements CanActivate {
    private router = inject(Router);

  canActivate(): boolean {
    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/login']);  // Redirige si no hay token
      return false;
    }
    return true;  // Si hay token, permite el acceso
  }
}
