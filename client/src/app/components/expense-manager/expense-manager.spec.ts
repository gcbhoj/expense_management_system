import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenseManager } from './expense-manager';

describe('ExpenseManager', () => {
  let component: ExpenseManager;
  let fixture: ComponentFixture<ExpenseManager>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpenseManager]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExpenseManager);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
