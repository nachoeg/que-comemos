import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MenuesServicio } from '../../../services/menues-servicio/menues-servicio';
import { Menu } from '../../../models/menu/menu';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu-edit',
  imports: [CommonModule],
  templateUrl: './menu-edit.component.html',
  styleUrl: './menu-edit.component.css'
})
export class MenuEditComponent implements OnInit{

  menuId: number = 0;
  menu: Menu = { // Initialize with empty properties
    id: 0,
    nombre: '',
    precio: 0,
    estructuras: [] // Empty array for estructuras
  };;

  constructor(private route: ActivatedRoute, private menuServicio: MenuesServicio) {}
  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const menuId = params?.get('id'); // Use optional chaining
      if (menuId) {
        this.menuId = +menuId;
        this.menuServicio.getMenuById(this.menuId).subscribe(menu => {
          this.menu = menu;
        });
      } else {
        // Handle the case where menuId is not found (optional)
        console.error('Menu ID not found in route parameters');
      }
    });
  }
}
