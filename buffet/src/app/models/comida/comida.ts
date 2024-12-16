export class Comida {
    public id: number;
    public nombre: string;
    public precio: number;
    public foto: string;
    public estructura: number;
    constructor (id: number, nombre: string, precio: number, foto: string, estructura: number){
      this.id = id;
      this.nombre = nombre;
      this.precio = precio;
      this.foto = foto;
      this.estructura = estructura;
      }
  }
