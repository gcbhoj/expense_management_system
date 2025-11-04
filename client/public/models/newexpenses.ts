export class NewExpense {
  expenseAmount!: number;
  expenseTitle!: string;
  expenseDescription!: string;
  expenseDate!: Date;

  expenseReceipt?: File;
}
