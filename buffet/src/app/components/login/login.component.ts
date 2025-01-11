import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

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

  constructor(public loginServicio: LoginServicio, private router: Router) {}

  login() {
    const user = {
      mail: this.email,
      clave: this.password
    };
    this.loginServicio.login(user).subscribe({
      next: (response) => {       
          console.log('Login successful!');
          this.router.navigate(['/']);
          if (response && response.user) {
            this.loginServicio.loginSuccess('¡Inicio de sesión exitoso!');
            this.errorMessage = ''; // Clear error message on successful login
          }
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.errorMessage = 'Error al iniciar sesión: ' + error.status;
      }
    });
  }
  

}
