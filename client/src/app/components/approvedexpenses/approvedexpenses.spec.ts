import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Approvedexpenses } from './approvedexpenses';

describe('Approvedexpenses', () => {
  let component: Approvedexpenses;
  let fixture: ComponentFixture<Approvedexpenses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Approvedexpenses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Approvedexpenses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
