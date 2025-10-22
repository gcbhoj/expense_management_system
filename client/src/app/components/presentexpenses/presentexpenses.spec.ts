import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Presentexpenses } from './presentexpenses';

describe('Presentexpenses', () => {
  let component: Presentexpenses;
  let fixture: ComponentFixture<Presentexpenses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Presentexpenses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Presentexpenses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
