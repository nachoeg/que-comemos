import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UsuariosService } from '../../../services/usuarios-service/usuarios-service';
import { Usuario } from '../../../models/usuario/usuario';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-perfil-editar',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './perfil-editar.component.html',
  styleUrl: './perfil-editar.component.css',
  providers: [UsuariosService],
})
export class PerfilEditarComponent {
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
      rol: new FormControl({ value: '', disabled: true }, [Validators.required]),
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
              rol: usuario.rol,
              foto: usuario.foto,
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
      // this.usuarioForm.patchValue({ foto: this.selectedFile?.name });
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
            rol: this.usuarioForm.get('rol')?.value,
          }),
        ],
        { type: 'application/json' }
      )
    );

    if (this.selectedFile) {
      formData.append('foto', this.selectedFile);
    } else {
      formData.append('foto', this.usuario.foto);
      // const fakeFile = new File([""], this.usuario.foto, { type: "image/jpeg" }); // Puedes cambiar "image/jpeg" según el tipo esperado
      // formData.append('foto', fakeFile);
    }

    this.usuariosService.updateUsuario(this.usuarioId, formData).subscribe(
      (response) => {
        this.successMessage = 'Usuario actualizado exitosamente!';
        console.log('Usuario actualizado:', response);
        this.isLoading = false;
        setTimeout(() => this.router.navigate(['/perfil']), 2000);
      },
      (error) => {
        this.errorMessage =
          error?.error?.message || 'Error al actualizar el usuario.';
        console.error('Error al actualizar el usuario:', error);
        this.isLoading = false;
      }
    );
  }

  getFilePreview() {
    return this.selectedFile ? URL.createObjectURL(this.selectedFile) : 'usuario.png';
  }
}
