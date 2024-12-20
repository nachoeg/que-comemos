import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers: [LoginServicio]
})
export class LoginComponent {
  email = '';  
  password = '';  

  constructor(public loginServicio: LoginServicio, private router: Router) {}

  login() {
    const user = {
      mail: this.email,
      clave: this.password
    };
    this.loginServicio.login(user).subscribe({
      next: (response) => {
        // const token = response.headers.get('token');
        // if (token) {
        //   localStorage.setItem('token', token);
          console.log('Login successful!');
          this.router.navigate(['/home']);
          // Redirect to a protected route or main application area
        // } else {
        //   console.error('Login failed!');
        // }
      },
      error: (error) => {
        console.error('Login failed:', error);
      }
    });
     
  }

}
