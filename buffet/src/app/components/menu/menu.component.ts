import { Component, OnInit } from '@angular/core';
import { MenuesServicio } from '../../services/menues-servicio/menues-servicio';
import { Menu } from '../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
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

  getRandomImageIndex() {
    return Math.floor(Math.random() * this.images.length);
  }


  menus: Menu[] = [];
  constructor(private menuService: MenuesServicio) { }

  ngOnInit() {
    this.menuService.getMenus().subscribe(menus => {
      this.menus = menus;
      this.isDetailVisible = new Array(menus.length).fill(false);
    });

  }

}
