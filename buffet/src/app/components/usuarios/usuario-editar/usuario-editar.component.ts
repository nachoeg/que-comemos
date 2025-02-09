import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UsuariosService } from '../../../services/usuarios-service/usuarios-service';
import { Usuario } from '../../../models/usuario/usuario';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-usuario-editar',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './usuario-editar.component.html',
  styleUrl: './usuario-editar.component.css',
  providers: [UsuariosService],
})
export class UsuarioEditarComponent implements OnInit {
  usuarioForm: FormGroup;
  usuarioId: number = 0;
  usuario: Usuario = new Usuario(0, 0, '', '', '', '', '', 0, '');
  selectedFile: File | null = null;
  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private usuariosService: UsuariosService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.usuarioForm = new FormGroup({
      dni: new FormControl({ value: '', disabled: true }, [Validators.required]),
      nombre: new FormControl('', [Validators.required]),
      apellido: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      clave: new FormControl('', [Validators.required, Validators.minLength(6)]),
      foto: new FormControl(),
      rol: new FormControl('', [Validators.required]),
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params?.get('id');
      if (id) {
        this.usuarioId = +id;
        this.usuariosService.getUsuario(this.usuarioId).subscribe(
          (usuario: Usuario) => {
            this.usuario = usuario;
            this.usuarioForm.patchValue({
              dni: usuario.dni,
              nombre: usuario.nombre,
              apellido: usuario.apellido,
              email: usuario.email,
              clave: usuario.clave,
              rol: usuario.rol,
            });
          },
          (error) => {
            console.error('Error al obtener usuario', error);
          }
        );
      } else {
        console.error('El ID del usuario no se encontró en los parámetros de la ruta');
      }
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onSubmit() {
    this.isLoading = true;
    const formData = new FormData();
    formData.append(
      'usuario',
      new Blob(
        [
          JSON.stringify({
            dni: this.usuarioForm.get('dni')?.value,
            nombre: this.usuarioForm.get('nombre')?.value,
            apellido: this.usuarioForm.get('apellido')?.value,
            email: this.usuarioForm.get('email')?.value,
            clave: this.usuarioForm.get('clave')?.value,
            rol: this.usuarioForm.get('rol')?.value,
          }),
        ],
        { type: 'application/json' }
      )
    );

    if (this.selectedFile) {
      formData.append('foto', this.selectedFile);
    }

    this.usuariosService.updateUsuario(this.usuarioId, formData).subscribe(
      (response) => {
        this.successMessage = 'Usuario actualizado exitosamente!';
        console.log('Usuario actualizado:', response);
        this.isLoading = false;
        setTimeout(() => this.router.navigate(['/usuarios']), 2000);
      },
      (error) => {
        this.errorMessage =
          error?.error?.message || 'Error al actualizar el usuario.';
        console.error('Error al actualizar el usuario:', error);
        this.isLoading = false;
      }
    );
  }
}
