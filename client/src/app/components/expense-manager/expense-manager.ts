import { Component } from '@angular/core';
import { Presentexpenses } from "../presentexpenses/presentexpenses";
import { Approvedexpenses } from "../approvedexpenses/approvedexpenses";
import { Pendingexpenses } from "../pendingexpenses/pendingexpenses";
import { Savedexpenses } from "../savedexpenses/savedexpenses";

@Component({
  selector: 'app-expense-manager',
  imports: [Presentexpenses, Approvedexpenses, Pendingexpenses, Savedexpenses],
  templateUrl: './expense-manager.html',
  styleUrl: './expense-manager.scss',
})
export class ExpenseManager {}
