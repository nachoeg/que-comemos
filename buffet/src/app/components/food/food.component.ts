import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FoodsService } from '../../services/foods-service/foods-service';
import { Food } from '../../models/food/food';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-food',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
  ],
  templateUrl: './food.component.html',
  styleUrls: ['./food.component.css'],
  providers: [FoodsService],
})
export class FoodComponent {
  comidas: Food[] = [];
  imageLoaded: boolean[] = [];
  imageError: boolean[] = [];

  constructor(private foodsService: FoodsService, private router: Router) {}

  ngOnInit() {
    this.foodsService.getFoods().subscribe((comidas) => {
      this.comidas = comidas;
      this.imageLoaded = new Array(comidas.length).fill(false);
      this.imageError = new Array(comidas.length).fill(false);
    });
  }

  editFood(foodId: number) {
    this.router.navigate(['/comidas', foodId, 'editar']);
  }

  onImageLoad(index: number): void {
    this.imageLoaded[index] = true;
    this.imageError[index] = false;
  }

  onImageError(index: number): void {
    this.imageLoaded[index] = false;
    this.imageError[index] = true;
  }

  deleteFood(foodId: number) {
    this.foodsService.deleteFood(foodId).subscribe(
      () => {
        // Handle successful deletion
        this.comidas = this.comidas.filter((comida) => comida.id !== foodId);
        console.log('Comida eliminada correctamente');
      },
      (error) => {
        // Handle errors
        console.error('Error al eliminar la comida', error);
      }
    );
  }
}
