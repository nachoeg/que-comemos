import { Component, ChangeDetectionStrategy, OnInit } from '@angular/core';
import { NewMenu } from '../../../models/new-menu/new-menu';
import { Estructura } from '../../../models/estructura/estructura';
import { Comida } from '../../../models/comida/comida';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';   
import { ReactiveFormsModule } from '@angular/forms';
import { MenuesServicio } from '../../../services/menues-servicio/menues-servicio';
import { Router } from '@angular/router'; 


@Component({
  selector: 'app-menu-create',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatIconModule, CommonModule, ReactiveFormsModule],
  templateUrl: './menu-create.component.html',
  styleUrl: './menu-create.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,  
  providers: [MenuesServicio],
})
export class MenuCreateComponent implements OnInit{
  menuForm: FormGroup; // Use a FormGroup for reactive forms

  createSuccessMessage: string = ''; // Initialize success message
  createErrorMessage: string = ''; // Initialize error message

  constructor(private menuServicio: MenuesServicio, private router: Router) {
    this.menuForm = new FormGroup({
      nombre: new FormControl('', Validators.required),
      precio: new FormControl('', [Validators.required, Validators.min(0)]), // Add min price validation
    });
  }

  ngOnInit() {
    
  }

  onSubmit() {
    // Access form values using formControlName or form.value
    const menu: NewMenu = {
      id: 0,
      nombre: this.menuForm.get('nombre')?.value,
      precio: this.menuForm.get('precio')?.value,
    };

    this.menuServicio.createMenu(menu).subscribe(
      (response) => {
        this.createSuccessMessage = 'Menú creado exitosamente!';
        console.log('Menú creado exitosamente:', response);
        setTimeout(() => {
          this.router.navigate(['/menu']); // Replace with your menu list route
        }, 2000);
      },
      (error) => {
        this.createErrorMessage = 'Error al crear el menú.';
        console.error('Error al crear el menú:', error);
      }
    );
  }
}