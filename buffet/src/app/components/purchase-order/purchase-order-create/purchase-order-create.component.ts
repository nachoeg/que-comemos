import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenusService } from '../../../services/menus-service/menus-service';
import { Menu } from '../../../models/menu/menu';
import { CartService } from '../../../services/cart-service/cart-service';
import { Order, OrderFood, OrderMenu } from '../../../models/order/order';
import { FoodsService } from '../../../services/foods-service/foods-service';
import { Food } from '../../../models/food/food';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-purchase-order-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './purchase-order-create.component.html',
  styleUrl: './purchase-order-create.component.css'
})
export class PurchaseOrderCreateComponent {

  cantidadPorMenu: { [menuId: number]: number } = {};
  cantidadPorComida: { [comidaId: number]: number } = {};

  order: Order = new Order(new Date(), 0, 'Pendiente', 1);
  menus: Menu[] = [];
  foods: Food[] = [];
  imageLoaded: boolean[] = [];
  imageError: boolean[] = [];
  diaSemana: string = '';

  constructor(private menusService: MenusService, private foodsService: FoodsService, private cartService: CartService) { }

  ngOnInit() {
    this.diaSemana = this.obtenerDiaSemana();

    this.menusService.getMenusPorDia(this.diaSemana).subscribe(menus => {
      this.menus = menus;
      this.imageLoaded = new Array(menus.length).fill(false);
      this.imageError = new Array(menus.length).fill(false);

      this.menus.forEach(menu => {
        this.cantidadPorMenu[menu.id] = 1;

        const storedCartItems = this.cartService.getCartItems();
        if (storedCartItems && storedCartItems.menus) { // Verifica si existen storedCartItems y storedCartItems.menus
          const existingOrderMenu = storedCartItems.menus.find(orderMenu => orderMenu.menu.id === menu.id);
          if (existingOrderMenu) {
            this.cantidadPorMenu[menu.id] = existingOrderMenu.cantidad;
          }
        }
      });
    });

    this.foodsService.getFoods().subscribe(foods => {
      this.foods = foods;
      this.imageLoaded = new Array(foods.length).fill(false);
      this.imageError = new Array(foods.length).fill(false);

      this.foods.forEach(comida => {
        this.cantidadPorComida[comida.id] = 1;

        const storedCartItems = this.cartService.getCartItems();
        if (storedCartItems && storedCartItems.foods) { // Verifica si existen storedCartItems y storedCartItems.foods
          const existingOrderFood = storedCartItems.foods.find(orderFood => orderFood.comida.id === comida.id);
          if (existingOrderFood) {
            this.cantidadPorComida[comida.id] = existingOrderFood.cantidad;
          }
        }
      });
    });
  }

  obtenerDiaSemana(): string {
    const dias = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
    const hoy = new Date();
    return dias[hoy.getDay()];
  }

  onImageLoad(i: number) {
    this.imageLoaded[i] = true;
  }

  onImageError(i: number) {
    this.imageError[i] = true;
  }

  agregarMenuAlPedido(menu: Menu) {
    const cantidadNumerica = this.cantidadPorMenu[menu.id];

    if (isNaN(cantidadNumerica) || cantidadNumerica < 1) {
      console.error("Cantidad inválida");
      return;
    }

    const existingOrderMenu = this.order.menus.find(orderMenu => orderMenu.menu.id === menu.id);

    if (existingOrderMenu) {
      existingOrderMenu.cantidad = cantidadNumerica;
    } else {
      const orderMenu = new OrderMenu(menu, cantidadNumerica);
      this.order.menus.push(orderMenu);
    }

    this.calcularTotalPedido();
    console.log("Pedido actual:", this.order);
    this.cartService.setCartItems(this.order.menus, this.order.comidas);
  }

  agregarComidaAlPedido(comida: Food) {
    const cantidadNumerica = this.cantidadPorComida[comida.id];

    if (isNaN(cantidadNumerica) || cantidadNumerica < 1) {
      console.error("Cantidad inválida");
      return;
    }

    const existingOrderFood = this.order.comidas.find(orderFood => orderFood.comida.id === comida.id);

    if (existingOrderFood) {
      existingOrderFood.cantidad = cantidadNumerica;
    } else {
      const orderFood = new OrderFood(comida, cantidadNumerica);
      this.order.comidas.push(orderFood);
    }

    this.calcularTotalPedido();
    console.log("Pedido actual:", this.order);
    this.cartService.setCartItems(this.order.menus, this.order.comidas);
  }

  calcularTotalPedido() {
    this.order.monto = 0;
    for (const orderMenu of this.order.menus) {
      this.order.monto += orderMenu.menu.precio * orderMenu.cantidad;
    }
    for (const orderFood of this.order.comidas) {
      this.order.monto += orderFood.comida.precio * orderFood.cantidad;
    }
  }
  eliminarMenu(index: number) {
    this.order.menus.splice(index, 1); // Elimina el elemento del array
    this.calcularTotalPedido(); // Recalcula el total
    this.cartService.setCartItems(this.order.menus, this.order.comidas); // Actualiza el carrito en el servicio
  }

  eliminarComida(index: number) {
    this.order.comidas.splice(index, 1); // Elimina el elemento del array
    this.calcularTotalPedido();// Recalcula el total
    this.cartService.setCartItems(this.order.menus, this.order.comidas);  // Actualiza el carrito en el servicio
  }

  }

