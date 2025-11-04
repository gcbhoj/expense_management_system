import { TestBed } from '@angular/core/testing';

import { ExpenseServices } from './expense-services';

describe('ExpenseServices', () => {
  let service: ExpenseServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenseServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
