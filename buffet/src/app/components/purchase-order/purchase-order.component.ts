import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { OrderService } from '../../services/order-service/order-service';
import { PedidoDetalles } from '../../models/order-response/order-response';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-purchase-order',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './purchase-order.component.html',
  styleUrl: './purchase-order.component.css',
})
export class PurchaseOrderComponent implements OnInit {
  pedidos: PedidoDetalles[] = [];
  userId: number | null = null;

  constructor(
    private router: Router,
    private orderService: OrderService,
    private loginService: LoginServicio
  ) {
    this.userId = this.loginService.getUserId();
  }

  ngOnInit() {
    if (this.userId !== null) {
      this.orderService.getUserOrders(this.userId).subscribe((data) => {
        this.pedidos = data.sort((a, b) => {
          const dateA = new Date(a.fecha);
          const dateB = new Date(b.fecha);
          return dateB.getTime() - dateA.getTime();
        });
      });
    }
  }

  formatearFecha(fechaArray: any): string {
    if (!fechaArray || fechaArray.length < 3) return '';

    const [year, month, day] = fechaArray;
    return `${day.toString().padStart(2, '0')}/${month
      .toString()
      .padStart(2, '0')}/${year}`;
  }
}
