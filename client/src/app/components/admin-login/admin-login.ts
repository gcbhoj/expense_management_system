import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminAuthService } from '../../services/admin-auth.service';

@Component({
  selector: 'app-admin-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.scss',
})
export class AdminLogin {
  email = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  constructor(
    private adminAuthService: AdminAuthService,
    private router: Router
  ) {
    // Redirect if already logged in
    if (this.adminAuthService.isLoggedIn()) {
      this.router.navigate(['/admin/dashboard']);
    }
  }

  onSubmit() {
    this.errorMessage = '';
    this.isLoading = true;

    if (!this.email || !this.password) {
      this.errorMessage = 'Please enter both email and password';
      this.isLoading = false;
      return;
    }

    const success = this.adminAuthService.login(this.email, this.password);

    if (success) {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.errorMessage = 'Invalid email or password';
      this.isLoading = false;
    }
  }
}
