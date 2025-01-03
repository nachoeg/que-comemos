import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FoodsService } from '../../../services/foods-service/foods-service';
import { Food } from '../../../models/food/food';
import { EstructuraService } from '../../../services/estructura-service/estructura-service';
import { Estructura } from '../../../models/estructura/estructura';

@Component({
  selector: 'app-estructura-add-food',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './estructura-add-food.component.html',
  styleUrl: './estructura-add-food.component.css',
  providers: [FoodsService, EstructuraService],
})
export class EstructuraAddFoodComponent {
  estructura: Estructura = {
    id: 0,
    nombre: '',
    comidas: [],
  };
  foods: Food[] = [];
  imageLoaded: boolean[] = [];
  imageError: boolean[] = [];

  constructor(
    private foodsService: FoodsService,
    private estructuraService: EstructuraService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.foodsService.getFoods().subscribe((foods) => {
      this.foods = foods;
      this.imageLoaded = new Array(foods.length).fill(false);
      this.imageError = new Array(foods.length).fill(false);
    });
    this.route.paramMap.subscribe((params) => {
      const estructuraId = params?.get('estructuraId');
      if (estructuraId) {
        this.estructuraService
          .getEstructuraById(+estructuraId)
          .subscribe((estructura: Estructura) => {
            this.estructura = estructura;
          });
      } else {
        console.error(
          'El ID de la estructura no se encontró en los parámetros de la ruta'
        );
      }
    });
  }

  onImageLoad(index: number): void {
    this.imageLoaded[index] = true;
    this.imageError[index] = false;
  }

  onImageError(index: number): void {
    this.imageLoaded[index] = false;
    this.imageError[index] = true;
  }

  addFood(foodId: number) {
    this.estructuraService
      .addFoodToEstructura(this.estructura.id, foodId)
      .subscribe(
        () => {
          console.log('Comida añadida correctamente');
          this.router.navigate(['../..'], { relativeTo: this.route });
        },
        (error) => {
          console.error('Error al añadir la comida', error);
        }
      );
  }
}
