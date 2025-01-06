import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MenusService } from '../../../services/menus-service/menus-service';
import { EstructuraService } from '../../../services/estructura-service/estructura-service';
import { Menu } from '../../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-menu-item',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './menu-item.component.html',
  styleUrl: './menu-item.component.css',
})
export class MenuItemComponent implements OnInit {
  createSuccessMessage: string = '';
  createErrorMessage: string = '';
  menuId: number = 0;
  menu: Menu = {
    id: 0,
    nombre: '',
    precio: 0,
    foto: '',
    estructuras: [],
  };

  constructor(
    private menuService: MenusService,
    private estructuraService: EstructuraService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const menuId = params?.get('id');
      if (menuId) {
        this.menuId = +menuId;
        this.menuService.getMenuById(this.menuId).subscribe((menu: Menu) => {
          this.menu = menu;
        });
      } else {
        console.error(
          'El ID del menú no se encontró en los parámetros de la ruta'
        );
      }
    });
  }

  addFoodToEstructura(estructuraId: number) {
    this.router.navigate([
      'menus',
      this.menuId,
      estructuraId,
      'agregar-comida',
    ]);
  }

  removeFoodFromEstructura(estructuraId: number, comidaId: number) {
    this.estructuraService
      .removeFoodFromEstructura(estructuraId, comidaId)
      .subscribe(
        () => {
          const estructura = this.menu.estructuras.find(
            (estructura) => estructura.id === estructuraId
          );
          if (estructura) {
            estructura.comidas = estructura.comidas.filter(
              (comida) => comida.id !== comidaId
            );
          }
        },
        (error) => {
          console.error('Error al eliminar la comida de la estructura', error);
        }
      );
  }

  addEstructura() {
    this.router.navigate(['menus', this.menuId, 'crear-estructura']);
  }

  deleteEstructura(estructuraId: number) {
    this.estructuraService.deleteEstructura(estructuraId).subscribe(
      () => {
        this.menu.estructuras = this.menu.estructuras.filter(
          (estructura) => estructura.id !== estructuraId
        );
      },
      (error) => {
        console.error('Error al eliminar la estructura', error);
      }
    );
  }
}
