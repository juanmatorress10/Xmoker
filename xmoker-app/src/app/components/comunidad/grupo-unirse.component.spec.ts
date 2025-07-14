import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrupoUnirseComponent } from './grupo-unirse.component';

describe('GrupoUnirseComponent', () => {
  let component: GrupoUnirseComponent;
  let fixture: ComponentFixture<GrupoUnirseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GrupoUnirseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GrupoUnirseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
