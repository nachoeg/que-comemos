import { Component } from '@angular/core';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { LoginServicio } from "../../services/login-servicio/login-servicio";
import { Usuario } from '../../models/usuario/usuario';
import { Location, CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-perfil',
  imports: [RouterModule, CommonModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent {
  usuario: Usuario | null = null;

  constructor(
    private usuarioService: UsuariosService,
    private location: Location,
    private authService: LoginServicio,
    private router: Router
  ) {}

  ngOnInit(){
    const user = this.authService.getUserLoggedIn();
    
    this.usuarioService.getUsuario(user.id).subscribe(usuario => {
      this.usuario = usuario;
    });
  }

  goBack(): void {
    this.router.navigate(['/perfil']);
  }

  editarUsuario(id: number): void {
    this.router.navigate(['/perfil/editar', id]);
  }
}
