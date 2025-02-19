import { Component, OnInit } from '@angular/core';
import { MenusService } from '../../services/menus-service/menus-service';
import { Menu } from '../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers: [MenusService],
})
export class MenuComponent implements OnInit {
  menus: Menu[] = [];

  constructor(private menuService: MenusService, private router: Router) {}

  ngOnInit() {
    this.menuService.getMenus().subscribe((menus) => {
      this.menus = menus;
    });
  }

  onImageLoad(menuId: number): void {
    const menu = this.menus.find((menu) => menu.id === menuId);
    if (menu) {
      menu.imageLoaded = true;
      menu.imageError = false;
    }
  }

  onImageError(index: number): void {
    const menu = this.menus[index];
    if (menu) {
      menu.imageLoaded = false;
      menu.imageError = true;
    }
  }

  deleteMenu(menuId: number) {
    this.menuService.deleteMenu(menuId).subscribe(
      () => {
        this.menus = this.menus.filter((menu) => menu.id !== menuId);
        console.log('Menú eliminado correctamente');
      },
      (error) => {
        console.error('Error al eliminar el menú:', error);
      }
    );
  }

  editMenu(menuId: number) {
    this.router.navigate(['/menus', menuId, 'editar']);
  }

  openMenu(menuId: number) {
    this.router.navigate(['/menus', menuId]);
  }
}
