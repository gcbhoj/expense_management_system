import { Component } from '@angular/core';
import { Presentexpenses } from "../presentexpenses/presentexpenses";

@Component({
  selector: 'app-expense-manager',
  imports: [Presentexpenses],
  templateUrl: './expense-manager.html',
  styleUrl: './expense-manager.scss',
})
export class ExpenseManager {}
