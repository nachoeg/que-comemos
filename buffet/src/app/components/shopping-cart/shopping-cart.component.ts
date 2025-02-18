import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartService } from '../../services/cart-service/cart-service';
import { Order, OrderMenu, OrderFood } from '../../models/order/order';
import { Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order-service/order-service';
import { Router } from '@angular/router';
import { LoginServicio } from '../../services/login-servicio/login-servicio';

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
  orderId: number | null = null;

  constructor(private cartService: CartService, private orderService: OrderService, private router: Router, private loginService: LoginServicio) {}

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

  finalizarPedido() {
    if (this.cartItems.menus.length === 0 && this.cartItems.foods.length === 0) {
      console.error("El carrito está vacío. Agregue productos antes de finalizar el pedido.");
      // You could also display a message to the user here.
      return; // Stop the order process
    }
    const fecha = new Date(); // Current date
    const monto = this.calcularTotal();
    const estado = 'PENDIENTE'; // Initial state
    const usuarioId = this.loginService.getUserId();
    if (!usuarioId) {
      console.error("User not logged in. Cannot create order.");
      this.router.navigate(['/login']);
      return;
  }
    const order = new Order(fecha, monto, estado, usuarioId);
    order.menus = this.cartItems.menus;
    order.comidas = this.cartItems.foods;

    this.orderService.createOrder(order).subscribe({
      next: (orderId: number) => {
        console.log('Pedido creado exitosamente. ID del pedido:', orderId);
        this.orderId = orderId; // Guarda el ID del pedido
        this.cartService.clearCart();
        this.mostrarConfirmacion = true; // Muestra la sección de confirmación
      },
      error: (error) => {
        console.error('Error al crear el pedido', error);
      }
    });
  }

  cerrarConfirmacion() {  // Función para cerrar la confirmación
    this.mostrarConfirmacion = false;
  }
}