import { CanActivate } from "@angular/router";
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginServicio } from "../login-servicio/login-servicio";
@Injectable()
export class AuthGuard implements CanActivate {
constructor(private authService: LoginServicio, private router: Router) {
}
canActivate() {
// If the user is not logged in we'll send them back to the home page
if (!this.authService.isLogged()) {
console.log('No estás logueado');
this.router.navigate(['/']);
return false;
}
return true;
}
}
