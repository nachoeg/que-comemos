import { Component } from '@angular/core';
import { Usuario } from '../../models/usuario/usuario';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; 
import { UsuariosServicio } from '../../services/usuarios-servicio/usuarios-servicio';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  providers: [UsuariosServicio]
})
export class RegisterComponent {

  usuario = new Usuario(0, 0, '', '', '', '', '', 0);
  claveConfirmar = '';

  constructor(private usuarioServicio: UsuariosServicio, private router: Router) {}

  register() {
    this.usuario.rol = 1;
    console.log(this.usuario.email);
    console.log(this.usuario.clave);
    this.usuarioServicio.registerUser(this.usuario).subscribe(
      (response) => {
        console.log('Registro exitoso:', response);
        setTimeout(() => {
          this.router.navigate(['/']);
        }, 2000);
      },
      (error) => {
        console.error('Error al registrar usuario:', error);
      }
    );

  }

}
