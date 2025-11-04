import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NewExpense } from '../../../public/models/newexpenses';
import { Observable } from 'rxjs';
import { ApprovedExpenses } from '../../../public/models/approvedexpenses';
import { map } from 'rxjs/operators';
import { PendingExpenses } from '../../../public/models/pendingexpenses';
import { SavedExpenses } from '../../../public/models/savedexpenses';

@Injectable({
  providedIn: 'root',
})
export class ExpenseServices {
  private apiUrl = 'http://localhost:8080/api/v1/expenses';

  constructor(private http: HttpClient) {}

  saveClaimExpenses(expense: NewExpense): Observable<any> {
    const formData = new FormData();
    const { expenseReceipt, ...expenseWithoutFile } = expense;

    formData.append('expense', JSON.stringify(expenseWithoutFile));

    // Append file only if it exists
    if (expenseReceipt) {
      formData.append('file', expenseReceipt);
    }

    return this.http.post(`${this.apiUrl}/testclaim/2`, formData); // No headers!
  }

  getApprovedExpenses(employeeId: number): Observable<ApprovedExpenses[]> {
    return this.http
      .get<{ status: number; data: ApprovedExpenses[] }>(
        `${this.apiUrl}/approved/2`
      )
      .pipe(
        map((response) => response.data) // extract the data array
      );
  }

  getPendingApprovals(employeeId: number): Observable<PendingExpenses[]> {
    return this.http.get<PendingExpenses[]>(`${this.apiUrl}/pending/2`);
  }

  getSavedExpenses(employeeId: number): Observable<SavedExpenses[]> {
    return this.http
      .get<{ status: number; message: SavedExpenses[] }>(
        `${this.apiUrl}/saved/${employeeId}`
      )
      .pipe(
        map((response) => response.message) // extract the array
      );
  }
}
