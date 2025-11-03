export class NewExpense {
  expenseAmount!: number;
  expenseType!: string;
  expenseDescription!: string;
  expenseDate!: Date;
  expenseReceipt!: File | null;
}
