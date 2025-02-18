import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../models/order/order';
import { NewOrder } from '../../models/new-order/new-order';

@Injectable({
    providedIn: 'root',
  })
export class OrderService {
    private baseUrl = 'http://localhost:8080/api/pedidos'; // API pedidos

    constructor(private http: HttpClient) {}

    createOrder(order: Order): Observable<any> {
        const pedido = this.mapToNewOrder(order); // mapeo de pedido a nuevo pedido(contiene solo los id de comida y menu)
        return this.http.post<number>(this.baseUrl, pedido);
      }
      

      private mapToNewOrder(order: Order): NewOrder {
        return {
          fecha: order.fecha,
          monto: order.monto,
          estado: order.estado,
          menus: order.menus.map(orderMenu => orderMenu.menu.id), // envia id de Menu
          comidas: order.comidas.map(orderFood => orderFood.comida.id), // envia id de Comida
          usuario: order.usuario
        };
      }


}
