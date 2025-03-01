export class Sugerencia {
    id?: number;
    tipo: string;
    descripcion: string;
    fecha: string;
    usuario: number;
  
    constructor(tipo: string, descripcion: string, fecha: string, usuario: number, id?: number) {
      this.tipo = tipo;
      this.descripcion = descripcion;
      this.fecha = fecha;
      this.usuario = usuario;
      this.id = id;
    }
  }
  