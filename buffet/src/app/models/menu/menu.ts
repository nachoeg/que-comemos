import { Estructura } from '../estructura/estructura';

export class Menu {
  id: number;
  nombre: string;
  precio: number;
  foto: string;
  dia: string;
  estructuras: Estructura[];

  constructor(
    id: number,
    nombre: string,
    precio: number,
    foto: string,
    dia: string,
    estructuras: Estructura[]
  ) {
    this.id = id;
    this.nombre = nombre;
    this.precio = precio;
    this.foto = foto;
    this.dia = dia;
    this.estructuras = estructuras;
  }
}
