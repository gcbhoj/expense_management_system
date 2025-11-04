import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ExpenseServices } from '../../service/expense-services';
import { SavedExpenses } from '../../../../public/models/savedexpenses';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-savedexpenses',
  imports: [CommonModule],
  templateUrl: './savedexpenses.html',
  styleUrl: './savedexpenses.scss',
})
export class Savedexpenses {
  constructor(private expenseService: ExpenseServices) {}

  savedExpenses: SavedExpenses[] = [];

  ngOnInit(): void {
    this.loadSavedExpenses();
  }

  loadSavedExpenses() {
    this.expenseService.getSavedExpenses(2).subscribe({
      next: (data: SavedExpenses[]) => {
        this.savedExpenses = data;
        console.log(this.savedExpenses);
      },
      error: (err) => console.error('Failed to load saved expenses', err),
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
