import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogroCardComponent } from './logro-card.component';

describe('LogroCardComponent', () => {
  let component: LogroCardComponent;
  let fixture: ComponentFixture<LogroCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogroCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogroCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
