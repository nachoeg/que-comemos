import { Component, OnInit } from '@angular/core';
import { MenuesServicio } from '../../services/menues-servicio/menues-servicio';
import { Menu } from '../../models/menu/menu';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
  providers: [ MenuesServicio ]
})
export class MenuComponent implements OnInit{

  menus: Menu[] = [];
  constructor(private menuService: MenuesServicio) { }

  ngOnInit(): void {
    this.menuService.getMenus().subscribe(menus => {
      this.menus = menus;
    });
  }

}
