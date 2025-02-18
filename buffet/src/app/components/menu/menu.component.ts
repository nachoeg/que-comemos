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
    FormsModule
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  providers: [MenusService],
})
export class MenuComponent implements OnInit {
  menus: Menu[] = [];
  imageLoaded: boolean[] = [];
  imageError: boolean[] = [];
  usuarioLogeado: any; // Store the logged-in user
  private destroy$ = new Subject<void>();
  cantidadPorMenu: { [menuId: number]: number } = {};

  constructor(private menuService: MenusService,
    private router: Router,
    private loginService: LoginServicio,
    private cartService: CartService) { }

  ngOnInit() {
    this.menuService.getMenus().subscribe((menus) => {
      this.menus = menus;
      this.imageLoaded = new Array(menus.length).fill(false);
      this.imageError = new Array(menus.length).fill(false);
      this.menus.forEach(menu => {
        this.cantidadPorMenu[menu.id] = this.cartService.getMenuQuantity(menu.id); // Obtiene la cantidad de menus del servicio
      });

    });

    this.loginService.isUserLoggedIn$.pipe(takeUntil(this.destroy$)).subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.usuarioLogeado = this.loginService.getUserLoggedIn();
      } else {
        this.usuarioLogeado = null; 
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onImageLoad(index: number): void {
    this.imageLoaded[index] = true;
    this.imageError[index] = false;
  }

  onImageError(index: number): void {
    this.imageLoaded[index] = false;
    this.imageError[index] = true;
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
        console.error("Cantidad inválida");
        return;
    }

    this.cartService.setMenuQuantity(menu.id, cantidadNumerica); // Actualiza la cantidad en el servicio
    const orderMenu = new OrderMenu(menu, cantidadNumerica);
    this.cartService.addItem(orderMenu, true);
    console.log("Menu agregado/actualizado en el carrito:", orderMenu);
    }
}
