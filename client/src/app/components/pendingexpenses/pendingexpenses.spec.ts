import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Pendingexpenses } from './pendingexpenses';

describe('Pendingexpenses', () => {
  let component: Pendingexpenses;
  let fixture: ComponentFixture<Pendingexpenses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Pendingexpenses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Pendingexpenses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
