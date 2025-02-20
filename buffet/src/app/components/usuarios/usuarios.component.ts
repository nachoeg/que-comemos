import { Component } from '@angular/core';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { Usuario } from '../../models/usuario/usuario';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-usuarios-component',
  imports: [RouterModule, CommonModule],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css',
})
export class UsuariosComponent {
  usuarios: Usuario[] = [];

  constructor(
    private usuarioService: UsuariosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    console.log('Cargando usuarios...');
    this.usuarioService.getUsuarios().subscribe((data) => {
      this.usuarios = data;
    });
  }

  verDetalle(id: number): void {
    this.router.navigate(['/usuarios', id]);
  }

  editarUsuario(id: number): void {
    this.router.navigate(['/usuarios/editar', id]); // Redirige a la vista de edición
  }

  eliminarUsuario(id: number): void {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      this.usuarioService.deleteUsuario(id).subscribe(() => {
        alert('Usuario eliminado correctamente');
        this.cargarUsuarios();
      });
    }
  }
}
