import { Component } from '@angular/core';
import { FoodsService } from '../../../services/foods-service/foods-service';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-food-create',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './food-create.component.html',
  styleUrls: ['./food-create.component.css'],
  providers: [FoodsService],
})
export class FoodCreateComponent {
  foodForm: FormGroup;
  createSuccessMessage: string = '';
  createErrorMessage: string = '';
  selectedFile: File | null = null;
  isLoading: boolean = false;

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

    this.foodsService.createFood(formData).subscribe(
      (response) => {
        this.createSuccessMessage = 'Menú creado exitosamente!';
        console.log('Menú creado exitosamente:', response);
        this.isLoading = false;
        this.router.navigate(['/comidas']);
      },
      (error) => {
        this.createErrorMessage = error?.error?.message ? error.error.message : 'Error al crear la comida.';
        // this.createErrorMessage = 'Error al crear el menú.';
        console.error('Error al crear el menú:', error);
        this.isLoading = false;
      }
    );
  }
}
