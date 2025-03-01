import { Component } from '@angular/core';
import { SugerenciasService } from '../../services/sugerencias-service/sugerencias-service';
import { Sugerencia } from '../../models/sugerencias/sugerencias';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sugerencias',
  imports: [RouterModule, CommonModule],
  templateUrl: './sugerencias.component.html',
  styleUrl: './sugerencias.component.css',
})
export class SugerenciasComponent {
  sugerencias: Sugerencia[] = [];
  rolName: string = '';

  constructor(
    private sugerenciasService: SugerenciasService,
    private router: Router,
    private authService: LoginServicio
  ) {}

  ngOnInit() {
    this.sugerenciasService.getSugerencias().subscribe((data) => {
      this.sugerencias = data.sort((a: Sugerencia, b: Sugerencia) => {
        const dateA = new Date(a.fecha);
        const dateB = new Date(b.fecha);
        return dateB.getTime() - dateA.getTime();
      });
    });

    const user = this.authService.getUserLoggedIn();
    this.rolName = user?.rolName || '';
  }

  verDetalle(id: number) {
    this.router.navigate(['/sugerencias', id]);
  }

  nuevaSugerencia(): void {
    this.router.navigate(['/sugerencias/crear']);
  }

  formatearFecha(fechaArray: any): string {
    if (!fechaArray || fechaArray.length < 3) return '';

    const [year, month, day] = fechaArray;
    return `${day.toString().padStart(2, '0')}/${month
      .toString()
      .padStart(2, '0')}/${year}`;
  }
}
