import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilFumadorComponent } from './perfil-fumador.component';

describe('PerfilFumadorComponent', () => {
  let component: PerfilFumadorComponent;
  let fixture: ComponentFixture<PerfilFumadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilFumadorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilFumadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
