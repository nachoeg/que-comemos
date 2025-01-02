import { Component } from '@angular/core';
import { FoodsService } from '../../../services/foods-service/foods-service';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { NewFood } from '../../../models/new-food/new-food';
import { CommonModule } from '@angular/common';
import { Food } from '../../../models/food/food';

@Component({
  selector: 'app-food-edit',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './food-edit.component.html',
  styleUrl: './food-edit.component.css',
  providers: [FoodsService],
})
export class FoodEditComponent {
  foodForm: FormGroup;
  createSuccessMessage: string = '';
  createErrorMessage: string = '';
  foodId: number = 0;
  food: Food = {
    id: 0,
    nombre: '',
    precio: 0,
    foto: '',
  };

  constructor(
    private foodsService: FoodsService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.foodForm = new FormGroup({
      nombre: new FormControl('', [
        Validators.required,
        Validators.nullValidator,
      ]),
      precio: new FormControl('', [Validators.required, Validators.min(0)]),
      foto: new FormControl(''),
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const foodId = params?.get('id');
      if (foodId) {
        this.foodId = +foodId;
        this.foodsService.getFoodById(this.foodId).subscribe((food: Food) => {
          this.food = food;
          this.foodForm.patchValue(this.food);
        });
      } else {
        console.error(
          'El ID de la comida no se encontró en los parámetros de la ruta'
        );
      }
    });
  }

  onSubmit() {
    const food: NewFood = {
      nombre: this.foodForm.get('nombre')?.value,
      precio: this.foodForm.get('precio')?.value,
      foto: this.foodForm.get('foto')?.value,
    };

    this.foodsService.updateFood(food, this.foodId).subscribe(
      (response) => {
        this.createSuccessMessage = 'Comida modificada exitosamente!';
        console.log('Comida modificada exitosamente:', response);
        this.router.navigate(['/comidas']);
      },
      (error) => {
        this.createErrorMessage = 'Error al modificar la comida.';
        console.error('Error al modificar la comida:', error);
      }
    );
  }
}
