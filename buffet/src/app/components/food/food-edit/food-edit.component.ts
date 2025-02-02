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
  food: NewFood = {
    nombre: '',
    precio: 0,
  };
  selectedFile: File | null = null;
  isLoading: boolean = false;

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
      foto: new FormControl(),
    });
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const foodId = params?.get('id');
      if (foodId) {
        this.foodId = +foodId;
        this.foodsService.getFoodById(this.foodId).subscribe((food: Food) => {
          this.food = food;
          this.foodForm.patchValue({
            nombre: food.nombre,
            precio: food.precio,
          });
        });
      } else {
        console.error(
          'El ID de la comida no se encontró en los parámetros de la ruta'
        );
      }
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  onSubmit() {
    this.isLoading = true;
    const formData = new FormData();
    formData.append(
      'comida',
      new Blob(
        [
          JSON.stringify({
            nombre: this.foodForm.get('nombre')?.value,
            precio: this.foodForm.get('precio')?.value,
          }),
        ],
        { type: 'application/json' }
      )
    );
    if (this.selectedFile) {
      formData.append('foto', this.selectedFile);
    }

    this.foodsService.updateFood(formData, this.foodId).subscribe(
      (response) => {
        this.createSuccessMessage = 'Comida modificada exitosamente!';
        console.log('Comida modificada exitosamente:', response);
        this.isLoading = false;
        this.router.navigate(['/comidas']);
      },
      (error) => {
        this.createErrorMessage = 'Error al modificar la comida.';
        console.error('Error al modificar la comida:', error);
        this.isLoading = false;
      }
    );
  }
}
