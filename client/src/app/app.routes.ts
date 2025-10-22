import { Routes } from '@angular/router';
import { ExpenseManager } from './components/expense-manager/expense-manager';
import { Aboutus } from './components/aboutus/aboutus';
import { Home } from './components/home/home';

export const routes: Routes = [
  { path: 'expense-manager', component: ExpenseManager },
  { path: 'aboutus', component: Aboutus },
  { path: 'home', component: Home },
];
