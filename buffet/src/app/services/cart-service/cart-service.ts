import { Injectable } from '@angular/core';
import { Order, OrderFood, OrderMenu } from '../../models/order/order';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<{ menus: OrderMenu[], foods: OrderFood[] }>({ menus: [], foods: [] });
  public cartItems$ = this.cartItemsSubject.asObservable(); // Observable para los items del carrito

  private cantidadItemsSubject = new BehaviorSubject<number>(0); // Nuevo: Subject para la cantidad
  public cantidadItems$ = this.cantidadItemsSubject.asObservable(); // Nuevo: Observable para la cantidad

  private foodQuantities: { [comidaId: number]: number } = {}; // Diccionario para guardar las cantidades de comidas
  private menuQuantities: { [menuId: number]: number } = {}; //Diccionario para guardar las cantidades de menus
  constructor() {
    this.loadCartFromLocalStorage(); // Carga el carrito al inicializar el servicio
    this.updateCantidadItems(); // Inicializa la cantidad de items
  }

  getFoodQuantity(comidaId: number): number {
    return this.foodQuantities[comidaId] || 0; // Devuelve la cantidad guardada o 1 si no existe
  }

  setFoodQuantity(comidaId: number, cantidad: number) {
    this.foodQuantities[comidaId] = cantidad;
    this.saveCartToLocalStorage(); 
  }

  getMenuQuantity(menuId: number): number {
    return this.menuQuantities[menuId] || 0; // Devuelve la cantidad guardada o 1 si no existe
  }

  setMenuQuantity(menuId: number, cantidad: number) {
    this.menuQuantities[menuId] = cantidad;
    this.saveCartToLocalStorage();
  }

  getCartItems(): { menus: OrderMenu[], foods: OrderFood[] } {
    return this.cartItemsSubject.value; // Obtiene el valor actual del BehaviorSubject
  }

  setCartItems(menus: OrderMenu[], foods: OrderFood[]) {
    const cartItems = { menus: menus, foods: foods }; // Crea objeto con ambos arrays

    this.cartItemsSubject.next(cartItems); // Emite el nuevo valor a través del BehaviorSubject
    this.saveCartToLocalStorage(); // Guarda en localStorage
    this.updateCantidadItems(); // Actualiza la cantidad de items
  }

  private addMenu(menuItem: OrderMenu) {
    const currentCart = this.getCartItems();
    const existingMenuIndex = currentCart.menus.findIndex(cartMenu => cartMenu.menu.id === menuItem.menu.id);

    if (existingMenuIndex > -1) {
      currentCart.menus[existingMenuIndex].cantidad = menuItem.cantidad;
    } else {
      currentCart.menus.push(menuItem);
    }

    this.setCartItems(currentCart.menus, currentCart.foods);
  }

  private addFood(foodItem: OrderFood) {
    const currentCart = this.getCartItems();
    const existingFoodIndex = currentCart.foods.findIndex(cartFood => cartFood.comida.id === foodItem.comida.id);

    if (existingFoodIndex > -1) {
        currentCart.foods[existingFoodIndex].cantidad = foodItem.cantidad; // actualizar existentes
    } else {
        currentCart.foods.push(foodItem); // agregar nueva comida
    }

    this.setCartItems(currentCart.menus, currentCart.foods);
  }


  addItem(item: OrderMenu | OrderFood, isMenu: boolean) {
    if (isMenu) {
      this.addMenu(item as OrderMenu);
    } else {
      this.addFood(item as OrderFood);
    }
  }

  removeItem(item: OrderMenu | OrderFood, isMenu: boolean) {
  const currentCart = this.getCartItems();
  const itemsArray = isMenu ? currentCart.menus : currentCart.foods;

    const itemIndex = itemsArray.findIndex(cartItem => {
      if (isMenu) {
        return (cartItem as OrderMenu).menu.id === (item as OrderMenu).menu.id; // Comparación por ID para menús
      } else {
        return (cartItem as OrderFood).comida.id === (item as OrderFood).comida.id; // Comparación por ID para comidas (con Type Assertion)
      }
    });

    if (itemIndex > -1) {
      if (isMenu) {
        const updatedMenus = currentCart.menus.filter((menu, index) => index !== itemIndex);
        currentCart.menus = updatedMenus;
        delete this.menuQuantities[(item as OrderMenu).menu.id]; // Delete quantity for menu
      } else {
        const updatedFoods = currentCart.foods.filter((food, index) => index !== itemIndex);
        currentCart.foods = updatedFoods;
        delete this.foodQuantities[(item as OrderFood).comida.id]; // Delete quantity for food
      }

      // ***CREA UN NUEVO OBJETO***
      const newCartItems = { menus: currentCart.menus, foods: currentCart.foods };
      this.cartItemsSubject.next(newCartItems); // Emite el NUEVO objeto
      this.saveCartToLocalStorage();
      this.updateCantidadItems();
    }
  }
  private saveCartToLocalStorage() {
    const cartData = {
      cartItems: this.cartItemsSubject.value,
      foodQuantities: this.foodQuantities,
      menuQuantities: this.menuQuantities
    };
    localStorage.setItem('cartData', JSON.stringify(cartData));
  }

  private loadCartFromLocalStorage() {
    const storedCartData = localStorage.getItem('cartData');
    if (storedCartData) {
      const cartData = JSON.parse(storedCartData);
      this.cartItemsSubject.next(cartData.cartItems);
      this.foodQuantities = cartData.foodQuantities || {}; // Maneja el caso de que no existan aún
      this.menuQuantities = cartData.menuQuantities || {};
    }
  }


  private updateCantidadItems() {
    const cartItems = this.getCartItems();
    const cantidad = cartItems.menus.length + cartItems.foods.length;
    this.cantidadItemsSubject.next(cantidad); // Emite la nueva cantidad
  }

}