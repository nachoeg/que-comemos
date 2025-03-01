import { Component } from '@angular/core';
import {
  ReactiveFormsModule,
  FormGroup,
  FormControl,
  Validators,
} from '@angular/forms';
import { SugerenciasService } from '../../../services/sugerencias-service/sugerencias-service';
import { LoginServicio } from '../../../services/login-servicio/login-servicio';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sugerencias-crear',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './sugerencias-crear.component.html',
  styleUrl: './sugerencias-crear.component.css',
})
export class SugerenciasCrearComponent {
  sugerenciaForm: FormGroup;
  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private sugerenciasService: SugerenciasService,
    private router: Router,
    public loginServicio: LoginServicio
  ) {
    this.sugerenciaForm = new FormGroup({
      usuario: new FormControl(
        { value: this.usuarioLogeado?.email, disabled: true },
        Validators.required
      ),
      tipo: new FormControl('Alimentos', [
        Validators.required,
        Validators.maxLength(255),
      ]),
      descripcion: new FormControl('', [
        Validators.required,
        Validators.maxLength(255),
      ]),
    });
  }

  onSubmit() {
    console.log('onSubmit', this.sugerenciaForm.value);
    console.log('onSubmit', this.usuarioLogeado?.id);
    if (this.sugerenciaForm.valid) {
      this.isLoading = true;
      const nuevaSugerencia = {
        ...this.sugerenciaForm.value,
        usuario: this.usuarioLogeado?.id,
        fecha: new Date().toISOString().split('T')[0], // Fecha en formato ISO (YYYY-MM-DD)
      };

      console.log('nuevaSugerencia', nuevaSugerencia);

      this.sugerenciasService.createSugerencia(nuevaSugerencia).subscribe(
        () => {
          this.successMessage = 'Sugerencia enviada correctamente';
          this.isLoading = false;
        },
        (error) => {
          this.errorMessage =
            error?.error?.message || 'Error al enviar la sugerencia';
          this.isLoading = false;
        }
      );
    }
  }

  get usuarioLogeado() {
    return this.loginServicio.getUserLoggedIn();
  }
}
