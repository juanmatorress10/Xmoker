import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsejosModalComponent } from './consejos-modal.component';

describe('ConsejosModalComponent', () => {
  let component: ConsejosModalComponent;
  let fixture: ComponentFixture<ConsejosModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsejosModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsejosModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
