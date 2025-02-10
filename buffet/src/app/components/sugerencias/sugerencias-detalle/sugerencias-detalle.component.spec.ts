import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SugerenciasDetalleComponent } from './sugerencias-detalle.component';

describe('SugerenciasDetalleComponent', () => {
  let component: SugerenciasDetalleComponent;
  let fixture: ComponentFixture<SugerenciasDetalleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SugerenciasDetalleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SugerenciasDetalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
