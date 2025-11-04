import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import * as bootstrap from 'bootstrap';
import { ExpenseServices } from '../../service/expense-services';
import { ApprovedExpenses } from '../../../../public/models/approvedexpenses';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-approvedexpenses',
  templateUrl: './approvedexpenses.html',
  styleUrls: ['./approvedexpenses.scss'], // fixed
  imports: [CommonModule],
})
export class Approvedexpenses implements OnInit {
  @ViewChild('successToast', { static: true }) successToast!: ElementRef;
  @ViewChild('errorToast', { static: true }) errorToast!: ElementRef;

  approvedExpenses: ApprovedExpenses[] = [];

  constructor(private expenseService: ExpenseServices) {}

  ngOnInit(): void {
    this.loadApprovedExpenses();
  }

  loadApprovedExpenses() {
    this.expenseService
      .getApprovedExpenses(2) // employeeId for testing
      .subscribe({
        next: (data: ApprovedExpenses[]) => {
          this.approvedExpenses = data;
        },
        error: (err) => {
          console.error(err);
        },
      });
  }
  toggleDetails(id: string) {
    const el = document.getElementById(id);
    if (el) {
      const collapse = bootstrap.Collapse.getOrCreateInstance(el);
      collapse.toggle();
    }
  }
}
