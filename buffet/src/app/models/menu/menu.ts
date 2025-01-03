import { Estructura } from '../estructura/estructura';

export class Menu {
  id: number;
  nombre: string;
  precio: number;
  foto: string;
  estructuras: Estructura[];

  constructor(
    id: number,
    nombre: string,
    precio: number,
    foto: string,
    estructuras: Estructura[]
  ) {
    this.id = id;
    this.nombre = nombre;
    this.precio = precio;
    this.foto = foto;
    this.estructuras = estructuras;
  }
}
