import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Presentexpenses } from "../presentexpenses/presentexpenses";
import { ExpenseService } from '../../services/expense.service';

@Component({
  selector: 'app-expense-manager',
  imports: [Presentexpenses, CommonModule],
  templateUrl: './expense-manager.html',
  styleUrl: './expense-manager.scss',
})
export class ExpenseManager implements OnInit {
  private readonly EMPLOYEE_ID = 1; // Hardcoded employee ID

  approvedExpenses: any[] = [];
  pendingExpenses: any[] = [];
  savedExpenses: any[] = [];
  
  isLoadingApproved = false;
  isLoadingPending = false;
  isLoadingSaved = false;

  constructor(private expenseService: ExpenseService) {}

  ngOnInit() {
    this.loadAllExpenses();
  }

  loadAllExpenses() {
    this.loadApprovedExpenses();
    this.loadPendingExpenses();
    this.loadSavedExpenses();
  }

  loadApprovedExpenses() {
    this.isLoadingApproved = true;
    this.expenseService.getApprovedExpenses(this.EMPLOYEE_ID).subscribe({
      next: (response) => {
        console.log('Approved expenses response:', response);
        // Backend returns: { status: 302, message: [...expenses] }
        if (response && response.message) {
          this.approvedExpenses = Array.isArray(response.message) ? response.message : [];
        }
        this.isLoadingApproved = false;
      },
      error: (error) => {
        console.error('Error loading approved expenses:', error);
        // 404 means empty list, which is okay
        if (error.status === 404) {
          this.approvedExpenses = [];
        }
        this.isLoadingApproved = false;
      },
    });
  }

  loadPendingExpenses() {
    this.isLoadingPending = true;
    this.expenseService.getPendingExpenses(this.EMPLOYEE_ID).subscribe({
      next: (response) => {
        console.log('Pending expenses response:', response);
        if (response && response.message) {
          this.pendingExpenses = Array.isArray(response.message) ? response.message : [];
        }
        this.isLoadingPending = false;
      },
      error: (error) => {
        console.error('Error loading pending expenses:', error);
        if (error.status === 404) {
          this.pendingExpenses = [];
        }
        this.isLoadingPending = false;
      },
    });
  }

  loadSavedExpenses() {
    this.isLoadingSaved = true;
    this.expenseService.getSavedExpenses(this.EMPLOYEE_ID).subscribe({
      next: (response) => {
        console.log('Saved expenses response:', response);
        if (response && response.message) {
          this.savedExpenses = Array.isArray(response.message) ? response.message : [];
        }
        this.isLoadingSaved = false;
      },
      error: (error) => {
        console.error('Error loading saved expenses:', error);
        if (error.status === 404) {
          this.savedExpenses = [];
        }
        this.isLoadingSaved = false;
      },
    });
  }
}
