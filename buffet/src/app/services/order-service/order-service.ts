import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Order } from '../../models/order/order';
import { NewOrder } from '../../models/new-order/new-order';
import {
  OrderResponse,
  PedidoDetalles,
} from '../../models/order-response/order-response';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseUrl = 'http://localhost:8080/api/pedidos'; // API pedidos

  constructor(private http: HttpClient) {}

  // createOrder(order: any): Observable<OrderResponse> {
  //     const pedido = this.mapToNewOrder(order);
  //     return this.http.post<OrderResponse>(this.baseUrl, pedido);
  // }
  createOrder(order: any): Observable<OrderResponse> {
    const pedido = this.mapToNewOrder(order);
    return this.http.post<OrderResponse>(this.baseUrl, pedido).pipe(
      map((response) => {
        const pedidoDetalles: PedidoDetalles = {
          id: response.pedido.id,
          fecha: response.pedido.fecha,
          monto: response.pedido.monto,
          estado: response.pedido.estado,
          menus: response.pedido.menus.map((menuPedido) => ({
            nombre: menuPedido.nombre,
            cantidad: menuPedido.cantidad,
            precio: menuPedido.precio,
          })),
          comidas: response.pedido.comidas.map((comidaPedido) => ({
            nombre: comidaPedido.nombre,
            cantidad: comidaPedido.cantidad,
            precio: comidaPedido.precio,
          })),
          usuario: response.pedido.usuario,
        };
        return {
          qrCodeImage: response.qrCodeImage,
          pedido: pedidoDetalles,
        };
      })
    );
  }

  private mapToNewOrder(order: Order): NewOrder {
    return {
      fecha: order.fecha,
      monto: order.monto,
      estado: order.estado,
      menus: order.menus.map((orderMenu) => ({
        id: orderMenu.menu.id,
        cantidad: orderMenu.cantidad,
      })), // <--- Mapea a objetos {id, cantidad}
      comidas: order.comidas.map((orderFood) => ({
        id: orderFood.comida.id,
        cantidad: orderFood.cantidad,
      })), // <--- Mapea a objetos {id, cantidad}
      usuario: order.usuario,
    };
  }

  updateOrderStatus(id: number, estado: string): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.baseUrl}/${id}/estado`, {
      estado,
    });
  }

  getOrders(): Observable<PedidoDetalles[]> {
    return this.http.get<PedidoDetalles[]>(this.baseUrl);
  }
}
