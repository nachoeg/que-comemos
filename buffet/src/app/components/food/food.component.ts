import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FoodsService } from '../../services/foods-service/foods-service';
import { Food } from '../../models/food/food';
import { CommonModule } from '@angular/common';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { Subject, takeUntil } from 'rxjs';
import { CartService } from '../../services/cart-service/cart-service';
import { OrderFood } from '../../models/order/order';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-food',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './food.component.html',
  styleUrls: ['./food.component.css'],
  providers: [FoodsService],
})
export class FoodComponent {
  comidas: Food[] = [];
  imageLoaded: boolean[] = [];
  imageError: boolean[] = [];
  usuarioLogeado: any; // Store the logged-in user
  private destroy$ = new Subject<void>(); // Subject for unsubscribing
  cantidadPorComida: { [comidaId: number]: number } = {};

  constructor(private foodsService: FoodsService, 
    private router: Router, 
    private loginService: LoginServicio, 
    private cartService: CartService) {}

  ngOnInit() {
    this.foodsService.getFoods().subscribe((comidas) => {
      this.comidas = comidas;
      this.imageLoaded = new Array(comidas.length).fill(false);
      this.imageError = new Array(comidas.length).fill(false);
      this.comidas.forEach(comida => {
        this.cantidadPorComida[comida.id] = this.cartService.getFoodQuantity(comida.id); // Obtiene la cantidad del servicio
      });
    });

    this.loginService.isUserLoggedIn$.pipe(takeUntil(this.destroy$)).subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.usuarioLogeado = this.loginService.getUserLoggedIn();
      } else {
        this.usuarioLogeado = null; // or handle the case when the user is not logged in
      }
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }


  editFood(foodId: number) {
    this.router.navigate(['/comidas', foodId, 'editar']);
  }

  onImageLoad(comidaId: number): void {
    const comida = this.comidas.find((comida) => comida.id === comidaId);
    if (comida) {
      comida.imageLoaded = true;
      comida.imageError = false;
    }
  }

  onImageError(comidaId: number): void {
    const comida = this.comidas.find((comida) => comida.id === comidaId);
    if (comida) {
      comida.imageLoaded = false;
      comida.imageError = true;
    }
  }

  deleteFood(foodId: number) {
    this.foodsService.deleteFood(foodId).subscribe(
      () => {
        this.comidas = this.comidas.filter((comida) => comida.id !== foodId);
        console.log('Comida eliminada correctamente');
      },
      (error) => {
        console.error('Error al eliminar la comida', error);
      }
    );
  }

  agregarComidaAlPedido(comida: Food) {
    const cantidadNumerica = this.cantidadPorComida[comida.id];

    if (isNaN(cantidadNumerica) || cantidadNumerica < 1) {
      console.error("Cantidad inválida");
      return;
    }

    const orderFood = new OrderFood(comida, cantidadNumerica);
    this.cartService.addItem(orderFood, false);
    this.cartService.setFoodQuantity(comida.id, cantidadNumerica); // Guarda la cantidad en el servicio
    console.log("Comida agregada/actualizada en el carrito:", orderFood);
  }

}
