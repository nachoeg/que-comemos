import { Component } from '@angular/core';
import { Usuario } from '../../models/usuario/usuario';
import { AbstractControl, FormGroup, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from '../../services/alert-service/alert.service';

export function MatchingPasswords(passwordControlName: string) {
  return (control: AbstractControl) => {
    const password = control.root?.get(passwordControlName)?.value;
    const confirmPassword = control.value;

    return (password === confirmPassword) ? null : { matchingPasswords: { required: true } };
  };
}

@Component({
  standalone: true,
  selector: 'app-register',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  providers: [UsuariosService]
})
export class RegisterComponent {

  usuario: Usuario;;
  claveConfirmar = '';
  errorMessage = '';
  submitted = false;
  f: FormGroup;
  registerSuccessMessage: string | null = null;
  registrationSuccessful: boolean = false;

  constructor(private usuarioServicio: UsuariosService, private router: Router, private fb: FormBuilder,  private alertService: AlertService) {
    this.f = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      confirmPassword: ['', [Validators.required, MatchingPasswords('password')]],
      dni: ['', Validators.required],
      nombre: ['', Validators.required],
      apellido: ['', Validators.required]
    });
    this.usuario = new Usuario(0, 0, '', '', '', '', '', 0);
  }

  register() {
    this.submitted = true;
    this.errorMessage = '';
    if (this.f.valid) {
      this.usuario.email = this.f.get('email')?.value;
      this.usuario.clave = this.f.get('password')?.value;
      this.usuario.dni = this.f.get('dni')?.value;
      this.usuario.nombre = this.f.get('nombre')?.value;
      this.usuario.apellido = this.f.get('apellido')?.value;
      this.usuario.rol = 3;
      console.log(this.usuario);
   
      this.usuarioServicio.registerUser(this.usuario).subscribe({
        next: (response) => {
          console.log('Registro exitoso:', response);
          this.registerSuccessMessage = 'El usuario se registro exitosamente, ya puede acceder al sistema';
          this.registrationSuccessful = true; // Set the flag to true
          this.alertService.showAlert('¡Usuario registrado exitosamente!', 'success');
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error al registrar usuario:', error);
          if (error.status === 409) {
              this.errorMessage = error.error.message || 'Error al registrar usuario. DNI duplicado.';
              this.alertService.showAlert('DNI duplicado, no se pudo crear usuario', 'danger');
          } else {
              this.errorMessage = 'Error al registrar usuario: ';
              this.alertService.showAlert('No se pudo registrar el usuario', 'danger');
          }
          this.registerSuccessMessage = null;
          this.registrationSuccessful = false;
      }
      });
    }
  }

  login(){
    this.router.navigate(['/iniciar-sesion']);
  }
}
