import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ExpenseServices } from '../../service/expense-services';
import { PendingExpenses } from '../../../../public/models/pendingexpenses';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-pendingexpenses',
  imports: [CommonModule],
  templateUrl: './pendingexpenses.html',
  styleUrl: './pendingexpenses.scss',
})
export class Pendingexpenses implements OnInit {
  constructor(private expenseService: ExpenseServices) {}

  pendingExpenses: PendingExpenses[] = [];

  ngOnInit(): void {
    this.loadPendingExpenses();
  }

  loadPendingExpenses() {
    this.expenseService.getPendingApprovals(2).subscribe({
      next: (data: any) => {
        this.pendingExpenses = data.message; // <-- extract array here
        console.log(this.pendingExpenses);
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
