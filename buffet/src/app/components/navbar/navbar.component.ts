import { Component, EventEmitter, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    CommonModule,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent implements OnInit {
  private _isLoggedIn: boolean = false;
  alertMessage = '';
  showSuccessAlert = false;
  private destroy$ = new Subject<void>();

  constructor(private router: Router,
    public loginServicio: LoginServicio) { }
  
  ngOnInit() {
    
    this.loginServicio.isUserLoggedIn$.pipe(takeUntil(this.destroy$)).subscribe(isLoggedIn => {
      this._isLoggedIn = isLoggedIn;
    });

    this.loginServicio.loginSuccessMessage$.pipe(takeUntil(this.destroy$)).subscribe(message => {
      if (message) {
        console.log("Mensaje recibido en Navbar:", message); 
        this.showAlert(message);
      }
    });

    
  }

  ngOnDestroy() { // Importante: Desuscribirse al destruir el componente
    this.destroy$.next();
    this.destroy$.complete();
  }

  logout() {
    this.router.navigate(['/']);
    this.loginServicio.logout();
    this.showAlert("Sesion cerrada correctamente");
  }
  showAlert(message: string) {
    console.log("showAlert llamado con mensaje:", message);
    this.showSuccessAlert = true;
    this.alertMessage = message;

    // Ocultar la alerta después de 3 segundos
    setTimeout(() => {
      this.showSuccessAlert = false;
    }, 3000);
  }

  get isLoggedIn(): boolean {
    return this.loginServicio.isLogged();
  }

  get usuarioLogeado() {
    return this.loginServicio.getUserLoggedIn();
  }
}
