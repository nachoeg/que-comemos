import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SugerenciasCrearComponent } from './sugerencias-crear.component';

describe('SugerenciasCrearComponent', () => {
  let component: SugerenciasCrearComponent;
  let fixture: ComponentFixture<SugerenciasCrearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SugerenciasCrearComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SugerenciasCrearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
