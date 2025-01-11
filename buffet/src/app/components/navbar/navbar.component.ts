import { Component, EventEmitter, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { CommonModule } from '@angular/common';
import { UsuariosServicio } from '../../services/usuarios-servicio/usuarios-servicio';

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
  constructor(private router: Router, 
    public loginServicio: LoginServicio, public usuarioServicio: UsuariosServicio) {}
  showSuccessAlert = false;
  alertMessage = '';
  ngOnInit() {
    // Inicializar la variable showSuccessAlert a false al cargar el componente
    //this.showSuccessAlert = false;
    this.loginServicio.loginSuccessMessage$.subscribe(message => {
      if (message) {
        this.showAlert(message);
      }
    });
    // Subscribe to registerSuccessMessage$ from UsuariosServicio
    this.usuarioServicio.registerSuccessMessage$.subscribe(message => {
      if (message) {
        this.showSuccessAlert = true;
        this.showAlert(message);

      }
    });
  }

  logout() {
    this.router.navigate(['/']);
    this.loginServicio.logout();
    this.showAlert("Sesion cerrada correctamente");
  }
  showAlert(message: string) {
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
