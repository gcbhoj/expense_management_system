import { Component, ElementRef, ViewChild } from '@angular/core';
import { PresentOptions } from '../../../../public/models/enums';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NewExpense } from '../../../../public/models/newexpenses';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-presentexpenses',
  imports: [FormsModule, CommonModule],
  templateUrl: './presentexpenses.html',
  styleUrl: './presentexpenses.scss',
})
export class Presentexpenses {
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
    // Validate Employee ID
    if (!this.newExpenses.employeeId) {
      this.showError('Please Enter Employee Id.');
      return;
    }

    // Validate Employee Name
    if (!this.newExpenses.employeeName) {
      this.showError('Employee Name cannot be empty');
      return;
    }
    //validate expense amount
    if (
      !this.newExpenses.expenseAmount ||
      this.newExpenses.expenseAmount <= 0
    ) {
      this.showError('Expense Amount cannot be empty or less than zero.');
      return;
    }

    // Validate Receipt
    if (!this.newExpenses.expenseReceipt) {
      this.showError('Please upload a receipt');
      return;
    }
    // validating expense type
    if (!this.newExpenses.expenseType) {
      this.showError('Expense Type cannot be empty.');
      return;
    }
    // validating expense description
    if (!this.newExpenses.expenseDescription) {
      this.showError('Expense description cannot be empty.');
      return;
    }
    // validating expense data
    if (!this.newExpenses.expenseDate) {
      this.showError('Expense Date cannot be empty');
      return;
    }
    if (
      this.newExpenses.expenseDate &&
      new Date(this.newExpenses.expenseDate) > new Date()
    ) {
      this.showError('Expense date cannot be in future');
      return;
    }

    console.log('Form submitted successfully:', this.newExpenses);
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
