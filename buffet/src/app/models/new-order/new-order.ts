export interface NewOrderItem { // Define una interfaz para los items del pedido
    id: number;
    cantidad: number;
}
export class NewOrder {
    fecha: Date;
    monto: number;
    estado: string;
    menus: NewOrderItem[];
    comidas: NewOrderItem[];
    usuario: number;
  
    constructor(fecha: Date, monto: number, estado: string, menus: NewOrderItem[], comidas: NewOrderItem[], usuario: number) 
    {
      this.fecha = fecha;
      this.monto = monto;
      this.estado = estado;
      this.menus = menus;
      this.comidas = comidas;
      this.usuario = usuario;
    }
}
