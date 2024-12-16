import { Comida } from "../comida/comida";

export class Estructura {
    id: number;
  nombre: string;
  comidas: Comida[];

  constructor(id: number, nombre: string, comidas: Comida[]) {
    this.id = id;
    this.nombre = nombre;
    this.comidas = comidas;
  }
}
