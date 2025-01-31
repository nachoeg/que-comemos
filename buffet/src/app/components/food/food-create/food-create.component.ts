import { Component } from '@angular/core';
import { FoodsService } from '../../../services/foods-service/foods-service';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NewFood } from '../../../models/new-food/new-food';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-food-create',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './food-create.component.html',
  styleUrl: './food-create.component.css',
  providers: [FoodsService],
})
export class FoodCreateComponent {
  foodForm: FormGroup;
  createSuccessMessage: string = '';
  createErrorMessage: string = '';

  constructor(private foodsService: FoodsService, private router: Router) {
    this.foodForm = new FormGroup({
      nombre: new FormControl('', [
        Validators.required,
        Validators.nullValidator,
      ]),
      precio: new FormControl('', [Validators.required, Validators.min(0)]),
      foto: new FormControl(''),
    });
  }

  ngOnInit() {}

  onSubmit() {
    // Access form values using formControlName or form.value
    const food: NewFood = {
      nombre: this.foodForm.get('nombre')?.value,
      precio: this.foodForm.get('precio')?.value,
      foto: this.foodForm.get('foto')?.value,
    };

    this.foodsService.createFood(food).subscribe(
      (response) => {
        this.createSuccessMessage = 'Menú creado exitosamente!';
        console.log('Menú creado exitosamente:', response);
        this.router.navigate(['/comidas']);
      },
      (error) => {
        this.createErrorMessage = error?.error?.message ? error.error.message : 'Error al crear la comida.';
        // this.createErrorMessage = 'Error al crear el menú.';
        console.error('Error al crear el menú:', error);
      }
    );
  }
}
