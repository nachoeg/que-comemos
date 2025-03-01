import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AlertService } from '../../services/alert-service/alert.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  // providers: [LoginServicio]
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(
    public loginServicio: LoginServicio,
    private router: Router,
    private alertService: AlertService
  ) {}

  login() {
    const user = {
      mail: this.email,
      clave: this.password,
    };
    this.loginServicio.login(user).subscribe({
      next: (response) => {
        console.log('Login successful!');
        this.router.navigate(['/']);
        if (response && response.user) {
          this.alertService.showAlert('¡Inicio de sesión exitoso!', 'success');
          this.errorMessage = ''; // Clear error message on successful login
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
        let errorMessage = 'Error al iniciar sesión.'; // Mensaje genérico

        if (error.error && error.error.message) {
          // Verifica si error.error y error.error.message existen
          errorMessage = error.error.message; // Asigna el mensaje específico del backend
        }
        this.errorMessage = error.status + ' - ' + errorMessage;
        //this.errorMessage = 'Error al iniciar sesión: ' + error.status + error.message;
      },
    });
  }
}
