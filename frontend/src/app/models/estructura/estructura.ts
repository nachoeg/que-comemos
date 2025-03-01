import { Food } from '../food/food';

export class Estructura {
  id: number;
  nombre: string;
  comidas: Food[];

  constructor(id: number, nombre: string, comidas: Food[]) {
    this.id = id;
    this.nombre = nombre;
    this.comidas = comidas;
  }
}
