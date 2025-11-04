import { Component, ElementRef, ViewChild } from '@angular/core';
import { PresentOptions } from '../../../../public/models/enums';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NewExpense } from '../../../../public/models/newexpenses';
import * as bootstrap from 'bootstrap';
import { ExpenseServices } from '../../service/expense-services';

@Component({
  selector: 'app-presentexpenses',
  imports: [FormsModule, CommonModule],
  templateUrl: './presentexpenses.html',
  styleUrl: './presentexpenses.scss',
})
export class Presentexpenses {
  constructor(private expenseService: ExpenseServices) {}

  @ViewChild('successToast', { static: true }) successToast!: ElementRef;
  @ViewChild('errorToast', { static: true }) errorToast!: ElementRef;

  presentOptions: PresentOptions[] = Object.values(PresentOptions);
  selectedOption: PresentOptions | '' = '';

  newExpenses: NewExpense = new NewExpense();
  successMessage = '';
  errorMessage = '';

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.newExpenses.expenseReceipt = input.files[0]; // ✅ This is a File instance
    }
  }

  submitForm() {
    if (
      !this.newExpenses.expenseAmount ||
      this.newExpenses.expenseAmount <= 0
    ) {
      this.showError('Expense Amount cannot be empty or less than zero.');
      return;
    }

    if (!this.newExpenses.expenseReceipt) {
      this.showError('Please upload a receipt.');
      return;
    }

    if (!this.newExpenses.expenseTitle) {
      this.showError('Expense Type cannot be empty.');
      return;
    }

    if (!this.newExpenses.expenseDescription) {
      this.showError('Expense Description cannot be empty.');
      return;
    }

    if (!this.newExpenses.expenseDate) {
      this.showError('Expense Date cannot be empty.');
      return;
    }

    if (new Date(this.newExpenses.expenseDate) > new Date()) {
      this.showError('Expense Date cannot be in the future.');
      return;
    }

    // ✅ Call backend through service
    this.expenseService.saveClaimExpenses(this.newExpenses).subscribe({
      next: (response) => {
        console.log('Expense submitted successfully:', response);
        this.showSuccess('Expense submitted successfully!');
        this.newExpenses = new NewExpense(); // ✅ Reset form
      },
      error: (error) => {
        console.error('Error submitting expense:', error);
        this.showError('Failed to submit expense. Please try again.');
      },
    });
  }

 

  showSuccess(message: string) {
    this.successMessage = message;
    const toast = new bootstrap.Toast(this.successToast.nativeElement);
    toast.show();
  }

  showError(message: string) {
    this.errorMessage = message;
    const toast = new bootstrap.Toast(this.errorToast.nativeElement);
    toast.show();
  }
}
