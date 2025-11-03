import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface Admin {
  email: string;
  name: string;
}

@Injectable({
  providedIn: 'root',
})
export class AdminAuthService {
  private currentAdminSubject: BehaviorSubject<Admin | null>;
  public currentAdmin: Observable<Admin | null>;

  // Hardcoded admin credentials (in production, this would be validated by backend)
  private readonly ADMIN_CREDENTIALS = {
    email: 'admin@expense.com',
    password: 'admin123',
  };

  constructor() {
    const storedAdmin = localStorage.getItem('currentAdmin');
    this.currentAdminSubject = new BehaviorSubject<Admin | null>(
      storedAdmin ? JSON.parse(storedAdmin) : null
    );
    this.currentAdmin = this.currentAdminSubject.asObservable();
  }

  public get currentAdminValue(): Admin | null {
    return this.currentAdminSubject.value;
  }

  login(email: string, password: string): boolean {
    // Simple authentication check (replace with API call in production)
    if (
      email === this.ADMIN_CREDENTIALS.email &&
      password === this.ADMIN_CREDENTIALS.password
    ) {
      const admin: Admin = {
        email: email,
        name: 'Administrator',
      };
      localStorage.setItem('currentAdmin', JSON.stringify(admin));
      this.currentAdminSubject.next(admin);
      return true;
    }
    return false;
  }

  logout() {
    localStorage.removeItem('currentAdmin');
    this.currentAdminSubject.next(null);
  }

  isLoggedIn(): boolean {
    return this.currentAdminValue !== null;
  }
}
