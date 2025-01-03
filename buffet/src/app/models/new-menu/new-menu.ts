export class NewMenu {
  nombre: string;
  precio: number;
  foto: string;

  constructor(id: number, nombre: string, precio: number, foto: string) {
    this.nombre = nombre;
    this.precio = precio;
    this.foto = foto;
  }
}
