import { Menu } from "../menu/menu";
import { Food } from "../food/food";
export class Order {
    id: number;
    fecha: Date;
    monto: number;
    estado: string;
    menus: OrderMenu[];
    comidas: OrderFood[];
    usuario: number;

    constructor(
        id: number,
        fecha: Date,
        monto: number,
        estado: string,
        usuario: number
    ) {
        this.id = 0;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
        this.menus = []; // Inicializa los arrays
        this.comidas = [];
        this.usuario = usuario;
    }
}

export class OrderMenu {
    menu: Menu;
    cantidad: number;

    constructor(menu: Menu, cantidad: number) {
        this.menu = menu;
        this.cantidad = cantidad;
    }
}

export class OrderFood {
    comida: Food;
    cantidad: number;

    constructor(comida: Food, cantidad: number) {
        this.comida = comida;
        this.cantidad = cantidad;
    }
}

