import { Component, OnInit } from '@angular/core';
import { MenuesServicio } from '../../services/menues-servicio/menues-servicio';
import { Menu } from '../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
  providers: [MenuesServicio],

})
export class MenuComponent implements OnInit {
  isDetailVisible: boolean[] = [];

  images = [
    "/buffet-1.jpg",
    "/buffet-2.jpg",
    "/buffet-3.jpg",
    "/buffet-4.jpg",
    "/buffet-5.jpg"
  ]

  imageIndex = 0;

  getImageUrl(index: number): string {
    return this.images[index % this.images.length];
  }


  menus: Menu[] = [];
  constructor(private menuServicio: MenuesServicio, private router: Router) { }

  ngOnInit() {
    this.menuServicio.getMenus().subscribe(menus => {
      this.menus = menus;
      this.isDetailVisible = new Array(menus.length).fill(false);
    });

  }
  
  navigateToNewMenu() {
    this.router.navigate(['/new-menu']);
  }

  deleteMenu(menuId: number) {
    this.menuServicio.deleteMenu(menuId).subscribe(
      () => {
        // Handle successful deletion
        this.menus = this.menus.filter(menu => menu.id !== menuId);
        console.log('Menú eliminado correctamente');
      },
      (error) => {
        // Handle errors
        console.error('Error al eliminar el menú:', error);
        // Display an error message to the user
      }
    );
  }

  editMenu(menuId: number) {
    this.router.navigate(['/menu', menuId, 'edit']); //recibe el Id del menu
  }


}
