import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EstructuraService } from '../../../services/estructura-service/estructura-service';
import { NewEstructura } from '../../../models/new-estructura/new-estructura';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-estructura-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './estructura-create.component.html',
  styleUrl: './estructura-create.component.css',
})
export class EstructuraCreateComponent implements OnInit {
  estructuraForm: FormGroup;
  createSuccessMessage: string = '';
  createErrorMessage: string = '';
  menuId: number = 0;

  constructor(
    private estructuraService: EstructuraService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.estructuraForm = new FormGroup({
      nombre: new FormControl('', Validators.required),
    });
    this.route.params.subscribe((params) => {
      this.menuId = params['menuId'];
    });
  }

  ngOnInit() {}

  onSubmit() {
    const estructura: NewEstructura = {
      nombre: this.estructuraForm.get('nombre')?.value,
    };

    this.estructuraService.createEstructura(this.menuId, estructura).subscribe(
      (response) => {
        let id = response;
        this.createSuccessMessage = 'Estructura creada exitosamente!';
        console.log('Estructura creada exitosamente:', id);
        this.router.navigate(['..'], { relativeTo: this.route });
      },
      (error) => {
        this.createErrorMessage = 'Error al crear la estructura.';
        console.error('Error al crear la estructura:', error);
      }
    );
  }
}
