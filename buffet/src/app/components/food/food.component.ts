import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FoodsService } from '../../services/foods-service/foods-service';
import { Food } from '../../models/food/food';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-food',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './food.component.html',
  styleUrls: ['./food.component.css'],
  providers: [FoodsService],
})
export class FoodComponent {
  comidas: Food[] = [];

  constructor(private foodsService: FoodsService, private router: Router) {}

  ngOnInit() {
    this.foodsService.getFoods().subscribe((comidas) => {
      this.comidas = comidas;
    });
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
}
