import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SugerenciasService } from '../../../services/sugerencias-service/sugerencias-service';
import { Sugerencia } from '../../../models/sugerencias/sugerencias';
import { Usuario } from '../../../models/usuario/usuario';
import { LoginServicio } from '../../../services/login-servicio/login-servicio';
import { UsuariosService } from '../../../services/usuarios-service/usuarios-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sugerencias-detalle',
  imports: [CommonModule, RouterModule],
  templateUrl: './sugerencias-detalle.component.html',
  styleUrl: './sugerencias-detalle.component.css',
})
export class SugerenciasDetalleComponent {
  sugerencia: Sugerencia | null = null;
  usuario: Usuario | null = null;

  constructor(
    private route: ActivatedRoute,
    public loginServicio: LoginServicio,
    private sugerenciasService: SugerenciasService,
    private usuarioService: UsuariosService
  ) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.sugerenciasService.getSugerencia(id).subscribe(
        (data) => {
          this.sugerencia = data;
          this.usuarioService.getUsuario(data.usuario).subscribe((usuario) => {
            this.usuario = usuario;
          });
        },
        (error) => {
          console.error('Error al obtener la sugerencia', error);
        }
      );
    }
  }

  get usuarioLogeado() {
    return this.loginServicio.getUserLoggedIn();
  }

  formatearFecha(fechaArray: any): string {
    if (!fechaArray || fechaArray.length < 3) return '';

    const [year, month, day] = fechaArray;
    return `${day.toString().padStart(2, '0')}/${month
      .toString()
      .padStart(2, '0')}/${year}`;
  }
}
