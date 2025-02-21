import { Component } from '@angular/core';
import {
  ComidaDetalle,
  MenuDetalle,
  PedidoDetalles,
} from '../../../models/order-response/order-response';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order-service/order-service';
import { AlertService } from '../../../services/alert-service/alert.service';

@Component({
  selector: 'app-menu-orders',
  imports: [CommonModule],
  templateUrl: './menu-orders.component.html',
  styleUrl: './menu-orders.component.css',
})
export class MenuOrdersComponent {
  pedidos: PedidoDetalles[] = [];
  pedidosFiltrados: PedidoDetalles[] = [];
  fechaFiltro: string = '';

  constructor(
    private orderService: OrderService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.orderService.getOrders().subscribe((data) => {
      this.pedidos = data;
      this.pedidosFiltrados = data;
      console.log(data);
      console.log(this.pedidos);
    });
  }

  cambiarEstado(pedido: PedidoDetalles, estado: string): void {
    this.orderService.updateOrderStatus(pedido.id, estado).subscribe(
      (response) => {
        console.log('Estado actualizado', response);
        pedido.estado = estado;
      },
      (error) => {
        console.error('Error al actualizar el estado', error);
        this.alertService.showAlert('Error al actualizar el estado', 'danger');
      }
    );
  }

  formatearFecha(fechaArray: any): string {
    if (!fechaArray || fechaArray.length < 3) return '';

    const [year, month, day] = fechaArray;
    return `${day.toString().padStart(2, '0')}/${month
      .toString()
      .padStart(2, '0')}/${year}`;
  }

  onFechaChange(event: any): void {
    this.fechaFiltro = event.target.value;
    this.filtrarPedidos();
  }

  filtrarPedidos(): void {
    if (this.fechaFiltro) {
      this.pedidosFiltrados = this.pedidos.filter((pedido) => {
        const fechaPedido = this.formatearFecha(pedido.fecha);
        return fechaPedido === this.fechaFiltro.split('-').reverse().join('/');
      });
    } else {
      this.pedidosFiltrados = this.pedidos;
    }
  }
}
