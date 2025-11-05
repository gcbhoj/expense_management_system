import { CommonModule } from '@angular/common';
import { Component, ElementRef } from '@angular/core';
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
        console.log('Saved Expenses', this.savedExpenses);
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

  submitExpenses(expenseId: number) {
    const employeeId = 2; // example: use logged-in user's ID or dynamic value

    this.expenseService.submitExpenses(employeeId, expenseId).subscribe({
      next: (response) => {
        console.log('Submission response:', response);
        alert(response.message || 'Receipt submitted successfully!');
        // Refresh the list to update status
        this.loadSavedExpenses();
      },
      error: (err) => {
        console.error('Error submitting expense:', err);
        alert('Failed to submit expense. Please try again later.');
        // this.showError('Failed to submit expense.')
      },
    });
  }
}
