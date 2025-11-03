import { Routes } from '@angular/router';
import { ExpenseManager } from './components/expense-manager/expense-manager';
import { Aboutus } from './components/aboutus/aboutus';
import { Home } from './components/home/home';
import { AdminLogin } from './components/admin-login/admin-login';
import { AdminDashboard } from './components/admin-dashboard/admin-dashboard';
import { adminAuthGuard } from './guards/admin-auth.guard';

export const routes: Routes = [
  { path: 'expense-manager', component: ExpenseManager },
  { path: 'aboutus', component: Aboutus },
  { path: 'home', component: Home },
  { path: 'admin/login', component: AdminLogin },
  {
    path: 'admin/dashboard',
    component: AdminDashboard,
    canActivate: [adminAuthGuard],
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
];
