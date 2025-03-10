import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilEditarComponent } from './perfil-editar.component';

describe('PerfilEditarComponent', () => {
  let component: PerfilEditarComponent;
  let fixture: ComponentFixture<PerfilEditarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilEditarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilEditarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
