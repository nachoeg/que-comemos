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
            menus: order.menus.map(orderMenu => ({ id: orderMenu.menu.id, cantidad: orderMenu.cantidad })), // <--- Mapea a objetos {id, cantidad}
            comidas: order.comidas.map(orderFood => ({ id: orderFood.comida.id, cantidad: orderFood.cantidad })), // <--- Mapea a objetos {id, cantidad}
            usuario: order.usuario
        };
      }


}
