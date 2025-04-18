import { Component, OnInit } from '@angular/core';
import { MenusService } from '../../services/menus-service/menus-service';
import { Menu } from '../../models/menu/menu';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { Subject, takeUntil } from 'rxjs';
import { CartService } from '../../services/cart-service/cart-service';
import { FormsModule } from '@angular/forms';
import { OrderMenu } from '../../models/order/order';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    FormsModule,
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers: [MenusService],
})
export class MenuComponent implements OnInit {
  menus: Menu[] = [];
  usuarioLogeado: any;
  private destroy$ = new Subject<void>();
  cantidadPorMenu: { [menuId: number]: number } = {};
  showAllMenus: boolean = false;

  constructor(
    private menuService: MenusService,
    private router: Router,
    private loginService: LoginServicio,
    private cartService: CartService
  ) {}

  ngOnInit() {
    this.loginService.isUserLoggedIn$
      .pipe(takeUntil(this.destroy$))
      .subscribe((isLoggedIn) => {
        this.usuarioLogeado = isLoggedIn
          ? this.loginService.getUserLoggedIn()
          : null;
        this.loadMenus();
      });
  }

  loadMenus() {
    if (this.usuarioLogeado?.rolName === 'USER') {
      this.menuService.getMenusActive().subscribe((menus) => {
        this.menus = menus;
        this.menus.forEach((menu) => {
          this.cantidadPorMenu[menu.id] = 1;
        });
      });
    } else {
      this.menuService.getMenus().subscribe((menus) => {
        this.menus = menus;
      });
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
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

  agregarMenuAlPedido(menu: Menu) {
    const cantidadNumerica = this.cantidadPorMenu[menu.id];

    if (isNaN(cantidadNumerica) || cantidadNumerica < 1) {
      console.error('Cantidad inválida');
      return;
    }

    this.cartService.setMenuQuantity(menu.id, cantidadNumerica); // Actualiza la cantidad en el servicio
    const orderMenu = new OrderMenu(menu, cantidadNumerica);
    this.cartService.addItem(orderMenu, true);
    console.log('Menu agregado/actualizado en el carrito:', orderMenu);
  }
}
