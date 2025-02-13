import { Injectable } from '@angular/core';
import { OrderFood, OrderMenu } from '../../models/order/order';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  constructor() { }

  getCartItems(): { menus: OrderMenu[], foods: OrderFood[] } {
    const cartItems = localStorage.getItem('cartItems');
    return cartItems ? JSON.parse(cartItems) : { menus: [], foods: [] }; // Devuelve objeto con arrays vacíos
  }

  setCartItems(menus: OrderMenu[], foods: OrderFood[]) {
    const cartItems = { menus: menus, foods: foods }; // Crea objeto con ambos arrays
    localStorage.setItem('cartItems', JSON.stringify(cartItems));
  }

  // Otros métodos: eliminarItem, actualizarCantidad, calcularTotal, etc.
}