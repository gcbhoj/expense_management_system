import { Component, ElementRef, ViewChild, Output, EventEmitter } from '@angular/core';
import { PresentOptions } from '../../../../public/models/enums';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NewExpense } from '../../../../public/models/newexpenses';
import * as bootstrap from 'bootstrap';
import { ReceiptService } from '../../services/receipt.service';
import { ExpenseService } from '../../services/expense.service';

@Component({
  selector: 'app-presentexpenses',
  imports: [FormsModule, CommonModule],
  templateUrl: './presentexpenses.html',
  styleUrl: './presentexpenses.scss',
})
export class Presentexpenses {
  @ViewChild('successToast', { static: true }) successToast!: ElementRef;
  @ViewChild('errorToast', { static: true }) errorToast!: ElementRef;
  @Output() expenseSubmitted = new EventEmitter<void>();

  presentOptions: PresentOptions[] = Object.values(PresentOptions);
  selectedOption: PresentOptions | '' = '';

  newExpenses: NewExpense = new NewExpense();
  successMessage = '';
  errorMessage = '';
  isProcessingReceipt = false;
  savedExpenseId: number | null = null; // Store the expense ID after saving

  // Hardcoded employee ID - employees cannot change this
  private readonly EMPLOYEE_ID = 1; // Can be 1 or 2

  constructor(
    private receiptService: ReceiptService,
    private expenseService: ExpenseService
  ) {}

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.newExpenses.expenseReceipt = file;

      // Process receipt automatically when file is selected
      this.processReceipt(file);
    }
  }

  clearReceipt() {
    // Clear the receipt from the model
    this.newExpenses.expenseReceipt = null;
    
    // Clear ONLY the auto-filled amount from OCR
    // Description is user-entered (explaining WHY the expense occurred), so keep it
    this.newExpenses.expenseAmount = 0;
    
    // Clear saved expense ID
    this.savedExpenseId = null;
    
    // Reset the file input
    const fileInput = document.getElementById('receiptFileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
    
    this.showSuccess('Receipt cleared. Amount reset. You can upload a new receipt.');
  }

  processReceipt(file: File) {
    this.isProcessingReceipt = true;

    this.receiptService.readReceipt(file).subscribe({
      next: (response) => {
        console.log('Receipt processed:', response);

        if (response.error) {
          this.showError(`Failed to process receipt: ${response.error}`);
          this.isProcessingReceipt = false;
          return;
        }

        // Extract data from response - matches backend format
        const receiptData = response.receipt_data;

        if (receiptData) {
          // Auto-fill ONLY the expense amount from total_amount field
          // Description should be manually entered by user to explain WHY the expense occurred
          if (receiptData['total_amount']) {
            this.newExpenses.expenseAmount = receiptData['total_amount'];
          }

          this.showSuccess(
            `Receipt processed! Found ${
              receiptData.items?.length || 0
            } items. Total: $${receiptData['total_amount'] || 0}. Please enter a description explaining this expense.`
          );
        }

        this.isProcessingReceipt = false;
      },
      error: (error) => {
        console.error('Error processing receipt:', error);
        this.showError(
          'Failed to process receipt. Please ensure Tesseract OCR is installed and the Python backend is running.'
        );
        this.isProcessingReceipt = false;
      },
    });
  }

  saveExpense() {
    // Validate expense amount
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

    // Validate expense type
    if (!this.newExpenses.expenseType) {
      this.showError('Expense Type cannot be empty.');
      return;
    }

    // Validate expense description
    if (!this.newExpenses.expenseDescription) {
      this.showError('Expense description cannot be empty.');
      return;
    }

    // Validate expense date
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

    // Create FormData for multipart submission
    const formData = new FormData();
    formData.append('file', this.newExpenses.expenseReceipt);
    
    // Create expense JSON object (matching Java Expenses bean)
    const expenseData = {
      expenseTitle: this.newExpenses.expenseType,
      expenseDescription: this.newExpenses.expenseDescription,
      expenseAmount: this.newExpenses.expenseAmount,
      expenseDate: this.newExpenses.expenseDate
    };
    
    // Add expense data as JSON string
    formData.append('expense', JSON.stringify(expenseData));

    // Save to backend with hardcoded employee ID
    this.expenseService.saveExpenseClaim(this.EMPLOYEE_ID, formData).subscribe({
      next: (response) => {
        console.log('Expense saved successfully:', response);
        
        // Store the expense ID from the response
        if (response && response.expenseId) {
          this.savedExpenseId = response.expenseId;
          this.showSuccess(`Expense saved successfully! ID: ${this.savedExpenseId}. You can now submit it for approval.`);
        } else {
          this.showSuccess('Expense saved successfully! You can now submit it for approval.');
        }
      },
      error: (error) => {
        console.error('Error saving expense:', error);
        this.showError('Failed to save expense. Please try again.');
      },
    });
  }

  submitExpense() {
    if (!this.savedExpenseId) {
      this.showError('Please save the expense first before submitting.');
      return;
    }

    // Submit the saved expense using PATCH endpoint
    this.expenseService.submitExpenseClaim(this.EMPLOYEE_ID, this.savedExpenseId).subscribe({
      next: (response) => {
        console.log('Expense submitted for approval:', response);
        this.showSuccess('Expense submitted for approval successfully!');
        
        // Emit event to notify parent component to refresh expense lists
        this.expenseSubmitted.emit();
        
        // Reset form after successful submission
        this.resetForm();
      },
      error: (error) => {
        console.error('Error submitting expense:', error);
        this.showError('Failed to submit expense for approval. Please try again.');
      },
    });
  }

  resetForm() {
    this.newExpenses = new NewExpense();
    this.savedExpenseId = null;
    const fileInput = document.getElementById('receiptFileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
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
