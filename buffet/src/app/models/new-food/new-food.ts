export class NewFood {
  public nombre: string;
  public precio: number;
  public foto: string;
  constructor(nombre: string, precio: number, foto: string) {
    this.nombre = nombre;
    this.precio = precio;
    this.foto = foto;
  }
}
