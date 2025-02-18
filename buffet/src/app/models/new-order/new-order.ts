export class NewOrder {
    fecha: Date;
    monto: number;
    estado: string;
    menus: number[];
    comidas: number[];
    usuario: number;
  
    constructor(fecha: Date, monto: number, estado: string, menus: number[], comidas: number[], usuario: number) 
    {
      this.fecha = fecha;
      this.monto = monto;
      this.estado = estado;
      this.menus = menus;
      this.comidas = comidas;
      this.usuario = usuario;
    }
}
