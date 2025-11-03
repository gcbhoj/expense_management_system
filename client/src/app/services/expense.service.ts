import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ExpenseService {
  private apiUrl = 'http://localhost:8080'; // Spring Boot backend URL

  constructor(private http: HttpClient) {}

  /**
   * Save expense claim with receipt (creates the expense)
   * @param employeeId Hardcoded employee ID (1 or 2)
   * @param expenseData Expense form data
   * @returns Observable with submission response containing expenseId
   */
  saveExpenseClaim(employeeId: number, expenseData: FormData): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/api/v1/expenses/claimExpenses/${employeeId}`,
      expenseData
    );
  }

  /**
   * Submit expense claim for approval
   * @param employeeId Hardcoded employee ID (1 or 2)
   * @param expenseId The expense ID returned from save
   * @returns Observable with submission response
   */
  submitExpenseClaim(employeeId: number, expenseId: number): Observable<any> {
    return this.http.patch(
      `${this.apiUrl}/api/v1/expenses/submit/${employeeId}/${expenseId}`,
      {}
    );
  }

  /**
   * Get all approved expenses for an employee
   * @param employeeId The employee ID
   * @returns Observable with list of approved expenses
   */
  getApprovedExpenses(employeeId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/api/v1/expenses/approved/${employeeId}`
    );
  }

  /**
   * Get all pending expenses for an employee
   * @param employeeId The employee ID
   * @returns Observable with list of pending expenses
   */
  getPendingExpenses(employeeId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/api/v1/expenses/pending/${employeeId}`
    );
  }

  /**
   * Get all saved (draft) expenses for an employee
   * @param employeeId The employee ID
   * @returns Observable with list of saved expenses
   */
  getSavedExpenses(employeeId: number): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/api/v1/expenses/saved/${employeeId}`
    );
  }
}
