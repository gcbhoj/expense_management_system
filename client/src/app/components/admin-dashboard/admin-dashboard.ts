import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AdminAuthService } from '../../services/admin-auth.service';

interface PendingExpense {
  id: number;
  employeeId: number;
  employeeName: string;
  amount: number;
  type: string;
  description: string;
  date: Date;
  receiptUrl?: string;
  status: 'pending' | 'approved' | 'rejected';
}

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss',
})
export class AdminDashboard implements OnInit {
  pendingExpenses: PendingExpense[] = [];
  approvedExpenses: PendingExpense[] = [];
  rejectedExpenses: PendingExpense[] = [];
  adminName = '';
  selectedTab: 'pending' | 'approved' | 'rejected' = 'pending';

  constructor(
    private adminAuthService: AdminAuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const admin = this.adminAuthService.currentAdminValue;
    if (admin) {
      this.adminName = admin.name;
    }
    this.loadExpenses();
  }

  loadExpenses() {
    // TODO: Replace with actual API call to backend
    // Example: this.http.get<PendingExpense[]>('/api/admin/expenses')
    //   .subscribe(expenses => {
    //     this.pendingExpenses = expenses.filter(e => e.status === 'pending');
    //     this.approvedExpenses = expenses.filter(e => e.status === 'approved');
    //     this.rejectedExpenses = expenses.filter(e => e.status === 'rejected');
    //   });
    // For now, arrays remain empty until backend integration
  }

  approveExpense(expense: PendingExpense) {
    // TODO: API call to update expense status
    // Example: this.http.post(`/api/admin/expenses/${expense.id}/approve`, {})
    //   .subscribe(() => this.loadExpenses());

    expense.status = 'approved';
    this.loadExpenses();
  }

  rejectExpense(expense: PendingExpense) {
    // TODO: API call to update expense status
    // Example: this.http.post(`/api/admin/expenses/${expense.id}/reject`, {})
    //   .subscribe(() => this.loadExpenses());

    expense.status = 'rejected';
    this.loadExpenses();
  }

  logout() {
    this.adminAuthService.logout();
    this.router.navigate(['/admin/login']);
  }

  selectTab(tab: 'pending' | 'approved' | 'rejected') {
    this.selectedTab = tab;
  }

  get currentExpenses(): PendingExpense[] {
    switch (this.selectedTab) {
      case 'pending':
        return this.pendingExpenses;
      case 'approved':
        return this.approvedExpenses;
      case 'rejected':
        return this.rejectedExpenses;
    }
  }
}
