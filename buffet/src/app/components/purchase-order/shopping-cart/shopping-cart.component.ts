import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartService } from '../../../services/cart-service/cart-service';
import { Order, OrderMenu, OrderFood } from '../../../models/order/order';
import { Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order-service/order-service';
import { Router } from '@angular/router';
import { LoginServicio } from '../../../services/login-servicio/login-servicio';
import { FoodsService } from '../../../services/foods-service/foods-service';
import { MenusService } from '../../../services/menus-service/menus-service';
import { OrderResponse, PedidoDetalles } from '../../../models/order-response/order-response';
import { NewOrder } from '../../../models/new-order/new-order';
import { AlertService } from '../../../services/alert-service/alert.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-shopping-cart',
  imports: [CommonModule],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {

  cartItems: { menus: OrderMenu[], foods: OrderFood[] } = { menus: [], foods: [] };
  private destroy$ = new Subject<void>();
  mostrarConfirmacion = false;

  mostrarTicket: boolean = false;
  fecha: Date | undefined;
  qrCodeImage: any;
  pedidoData: PedidoDetalles | undefined;
  id = 0;


  constructor(private cartService: CartService,
    private orderService: OrderService,
    private router: Router,
    private loginService: LoginServicio,
    private foodService: FoodsService,
    private menuService: MenusService,
    private alertService: AlertService) { }

  ngOnInit() {
    this.cartService.cartItems$.pipe(takeUntil(this.destroy$)).subscribe(items => {
      this.cartItems = items;
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  eliminarMenu(index: number) {
    this.cartService.removeItem(this.cartItems.menus[index], true);
  }

  eliminarComida(index: number) {
    this.cartService.removeItem(this.cartItems.foods[index], false);
  }

  calcularTotal() {
    let total = 0;
    this.cartItems.menus.forEach(item => total += item.menu.precio * item.cantidad);
    this.cartItems.foods.forEach(item => total += item.comida.precio * item.cantidad);
    return total;
  }

  getCantidadComidas() {
    return this.cartItems.foods.reduce((total, foodItem) => total + foodItem.cantidad, 0);
  }

  getCantidadMenus() {
    return this.cartItems.menus.reduce((total, menuItem) => total + menuItem.cantidad, 0);
  }


  realizarPedido() {
    if (this.cartItems.menus.length === 0 && this.cartItems.foods.length === 0) {
      console.error("El carrito está vacío. Agregue productos antes de finalizar el pedido.");
      this.alertService.showAlert('El carrito está vacío. Agregue productos antes de finalizar el pedido.', 'danger'); 

      return;
    }

    this.fecha = new Date(); // Current date
    const monto = this.calcularTotal();
    const estado = 'PENDIENTE'; // Initial state
    const usuarioId = this.loginService.getUserId();
    if (!usuarioId) {
      console.error("User not logged in. Cannot create order.");
      this.alertService.showAlert('Debe iniciar sesión para realizar un pedido.', 'danger');
      this.router.navigate(['/login']);
      return;
    }

    const order = new Order(this.id, this.fecha, monto, estado, usuarioId);
    order.menus = this.cartItems.menus;
    order.comidas = this.cartItems.foods;

    this.orderService.createOrder(order).subscribe({
      next: (response) => {
        console.log('Pedido creado exitosamente', response);
        this.qrCodeImage = "data:image/png;base64," + response.qrCodeImage;
        this.pedidoData = response.pedido;

        this.cartService.clearCart();
        this.mostrarConfirmacion = true;
        this.mostrarTicket = true;
        this.alertService.showAlert('¡Pedido creado con exito! Se ha enviado un mail con el comprobante.', 'success');
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error al crear el pedido', error);
    if (error.error && error.error.message) {
        this.alertService.showAlert(error.error.message, 'danger');
    } else {
        this.alertService.showAlert('Ocurrió un error inesperado.', 'danger');
    }
      }
    });

  }

  cerrarConfirmacion() {  // Función para cerrar la confirmación
    this.mostrarConfirmacion = false;
    this.mostrarTicket = false;

  }

  imprimirTicket() {
    window.print();
  }
}