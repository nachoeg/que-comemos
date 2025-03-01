export interface OrderResponse {
  qrCodeImage: ArrayBuffer;
  pedido: PedidoDetalles;
}

export class PedidoDetalles {
  id: number;
  fecha: Date;
  monto: number;
  estado: string;
  menus: MenuDetalle[];
  comidas: ComidaDetalle[];
  usuario: number;

  constructor(
    id: number,
    fecha: Date,
    monto: number,
    estado: string,
    menus: MenuDetalle[],
    comidas: ComidaDetalle[],
    usuario: number
  ) {
    this.id = id;
    this.fecha = fecha;
    this.monto = monto;
    this.estado = estado;
    this.menus = menus;
    this.comidas = comidas;
    this.usuario = usuario;
  }
}

export class MenuDetalle {
  nombre: string;
  cantidad: number;
  precio: number;

  constructor(nombre: string, cantidad: number, precio: number) {
    this.nombre = nombre;
    this.cantidad = cantidad;
    this.precio = precio;
  }
}

export class ComidaDetalle {
  nombre: string;
  cantidad: number;
  precio: number;

  constructor(nombre: string, cantidad: number, precio: number) {
    this.nombre = nombre;
    this.cantidad = cantidad;
    this.precio = precio;
  }
}
