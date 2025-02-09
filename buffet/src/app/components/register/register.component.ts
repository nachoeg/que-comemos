import { Component } from '@angular/core';
import { Usuario } from '../../models/usuario/usuario';
import { AbstractControl, FormGroup, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

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

  constructor(private usuarioServicio: UsuariosService, private router: Router, private fb: FormBuilder) {
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
    if (this.f.valid) {
      this.usuario.email = this.f.get('email')?.value;
      this.usuario.clave = this.f.get('password')?.value;
      this.usuario.dni = this.f.get('dni')?.value;
      this.usuario.nombre = this.f.get('nombre')?.value;
      this.usuario.apellido = this.f.get('apellido')?.value;
      this.usuario.rol = 3;
      console.log(this.usuario);
      this.usuarioServicio.registerUser(this.usuario).subscribe(
        (response) => {
          console.log('Registro exitoso:', response);
          this.usuarioServicio.registerSuccess('Registro de usuario exitoso')
          setTimeout(() => {
            this.router.navigate(['/iniciar-sesion']);
          }, 2000);
        },
        (error) => {
          console.error('Error al registrar usuario:', error);
          this.errorMessage = 'Error al registrar usuario: ' + error.status;
        }
      );
    }
  }



}
