import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricasAvanzadasComponent } from './metricas-avanzadas.component';
import { NavbarInferiorComponent } from '../../navbar-inferior.component';

describe('MetricasAvanzadasComponent', () => {
  let component: MetricasAvanzadasComponent;
  let fixture: ComponentFixture<MetricasAvanzadasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetricasAvanzadasComponent, NavbarInferiorComponent], // Asegúrate de importar el componente NavbarInferiorComponent aquí
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetricasAvanzadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
