import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EstadisticasDiarioComponent } from './estadisticas-diario.component';

describe('EstadisticasDiarioComponent', () => {
  let component: EstadisticasDiarioComponent;
  let fixture: ComponentFixture<EstadisticasDiarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EstadisticasDiarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EstadisticasDiarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
