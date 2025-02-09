import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuariosService } from '../../../services/usuarios-service/usuarios-service';
import { Usuario } from '../../../models/usuario/usuario';
import { Location, CommonModule } from '@angular/common';


@Component({
  selector: 'app-usuario-detalle',
  imports: [CommonModule],
  templateUrl: './usuario-detalle.component.html',
  styleUrl: './usuario-detalle.component.css'
})
export class UsuarioDetalleComponent {
  usuario: Usuario | null = null;

  constructor(
    private route: ActivatedRoute,
    private usuarioService: UsuariosService,
    private location: Location
  ) {}

  ngOnInit(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.usuarioService.getUsuario(id).subscribe(usuario => {
        this.usuario = usuario;
      });
    }
  }

  goBack(): void {
    this.location.back();
  }
}
