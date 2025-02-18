import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartService } from '../../services/cart-service/cart-service';
import { OrderMenu, OrderFood } from '../../models/order/order';
import { Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-shopping-cart',
  imports: [CommonModule],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {

  cartItems: { menus: OrderMenu[], foods: OrderFood[] } = { menus: [], foods: [] };
  private destroy$ = new Subject<void>();

  constructor(private cartService: CartService) {}

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
}