import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MenusService } from '../../../services/menus-service/menus-service';
import { Menu } from '../../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { NewMenu } from '../../../models/new-menu/new-menu';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-menu-edit',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './menu-edit.component.html',
  styleUrl: './menu-edit.component.css',
})
export class MenuEditComponent implements OnInit {
  menuForm: FormGroup;
  createSuccessMessage: string = '';
  createErrorMessage: string = '';
  menuId: number = 0;
  menu: NewMenu = {
    nombre: '',
    precio: 0,
  };
  selectedFile: File | null = null;

  constructor(
    private menuService: MenusService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.menuForm = new FormGroup({
      nombre: new FormControl('', [
        Validators.required,
        Validators.nullValidator,
      ]),
      precio: new FormControl('', [Validators.required, Validators.min(0)]),
      foto: new FormControl(''),
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const menuId = params?.get('id');
      if (menuId) {
        this.menuId = +menuId;
        this.menuService.getMenuById(this.menuId).subscribe((menu: Menu) => {
          this.menu = menu;
          this.menuForm.patchValue({
            nombre: menu.nombre,
            precio: menu.precio,
          });
        });
      } else {
        console.error(
          'El ID del menú no se encontró en los parámetros de la ruta'
        );
      }
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onSubmit() {
    const formData = new FormData();
    formData.append(
      'menu',
      new Blob(
        [
          JSON.stringify({
            nombre: this.menuForm.get('nombre')?.value,
            precio: this.menuForm.get('precio')?.value,
          }),
        ],
        { type: 'application/json' }
      )
    );

    if (this.selectedFile) {
      formData.append('foto', this.selectedFile);
    }

    this.menuService.updateMenu(this.menuId, formData).subscribe(
      (response) => {
        this.createSuccessMessage = 'Menú actualizado correctamente';
        this.createErrorMessage = '';
        console.log('Menú actualizado correctamente:', response);
        this.router.navigate(['/menus']);
      },
      (error) => {
        this.createErrorMessage = 'Error al actualizar el menú';
        this.createSuccessMessage = '';
        console.error('Error al actualizar el menú:', error);
      }
    );
  }
}
