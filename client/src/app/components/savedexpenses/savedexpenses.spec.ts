import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Savedexpenses } from './savedexpenses';

describe('Savedexpenses', () => {
  let component: Savedexpenses;
  let fixture: ComponentFixture<Savedexpenses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Savedexpenses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Savedexpenses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
