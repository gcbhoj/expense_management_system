package ca.sheridancollege.ghartich.bootstrap;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.ghartich.beans.ApplicationStatus;
import ca.sheridancollege.ghartich.beans.ApprovalStatus;
import ca.sheridancollege.ghartich.beans.Employee;
import ca.sheridancollege.ghartich.beans.EmployeeRole;
import ca.sheridancollege.ghartich.beans.ExpenseItems;
import ca.sheridancollege.ghartich.beans.ExpenseList;
import ca.sheridancollege.ghartich.beans.Expenses;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseItemsRepository;
import ca.sheridancollege.ghartich.repository.ExpenseListRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmployeeBootstrap implements CommandLineRunner {
	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	private final ExpenseListRepository expenseListRepo;
	private final ExpenseItemsRepository expenseItemsRepo;
	
	@Override
	public void run(String... arg) throws Exception{
		
		
		Employee emp1 = Employee.builder()
				.employeeName("John Doe")
				.role(EmployeeRole.ADMIN)
				.expenses(new ArrayList<Expenses>())
				.build();
		Employee emp2 = Employee.builder()
				.employeeName("Jack Rack")
				.role(EmployeeRole.MANAGER)
				.expenses(new ArrayList<Expenses>())
				.build();
		Employee emp3 = Employee.builder()
				.employeeName("Jane Smith")
				.role(EmployeeRole.EMPLOYEE)
				.expenses(new ArrayList<Expenses>())
				.build();
		
		




		
		ExpenseItems expItems1 =  ExpenseItems.builder()
				.expenseItemName("Office Supplies")
				.expenseItemCost(200.20)
				.build();
		
		expItems1 =  expenseItemsRepo.save(expItems1);
		
		ExpenseList expList1 = ExpenseList.builder()
				.expenseItems(new ArrayList<>())
				.receivedTotalAmount(200.20)
				.build();
		expList1.getExpenseItems().add(expItems1);
		expList1 = expenseListRepo.save(expList1);
		
		Expenses exp1 = Expenses.builder()
			    .expenseTitle("Office Supplies")
			    .expenseDescription("Printer ink and stationery")
			    .expenseDate(LocalDate.now().minusDays(1))
			    .expenseAmount(200.20)
			    .approvalStatus(ApprovalStatus.PENDING)
			    .applicationStatus(ApplicationStatus.SAVED)
			    .storageId("ST001")
			    .expenseList(expList1)
			    .build();
		
		ExpenseItems expItems2 =  ExpenseItems.builder()
				.expenseItemName("Travel Expenses")
				.expenseItemCost(200.20)
				.build();
		
		expItems2 =  expenseItemsRepo.save(expItems2);
		
		ExpenseList expList2 = ExpenseList.builder()
				.expenseItems(new ArrayList<>())
				.receivedTotalAmount(200.20)
				.build();
		expList2.getExpenseItems().add(expItems2);
		expList2 = expenseListRepo.save(expList2);
		
		Expenses exp2 = Expenses.builder()
			    .expenseTitle("Travel Expenses")
			    .expenseDescription("Flight to Ottawa for client meeting")
			    .expenseDate(LocalDate.now().minusMonths(1))
			    .expenseAmount(2000.20)
			    .approvalStatus(ApprovalStatus.PENDING)
			    .applicationStatus(ApplicationStatus.SUBMITTED)
			    .storageId("ST002")
			    .expenseList(expList2)
			    .build();
		
		ExpenseItems expItems3 =  ExpenseItems.builder()
				.expenseItemName("Accommodation")
				.expenseItemCost(2000.00)
				.build();
		
		expItems3 =  expenseItemsRepo.save(expItems3);
		
		ExpenseList expList3 = ExpenseList.builder()
				.expenseItems(new ArrayList<>())
				.receivedTotalAmount(200.20)
				.build();
		expList3.getExpenseItems().add(expItems3);
		expList3= expenseListRepo.save(expList3);
		
		Expenses exp3 = Expenses.builder()
			    .expenseTitle("Accommodation")
			    .expenseDescription("Hotel stay for business trip")
			    .expenseDate(LocalDate.now().minusMonths(1))
			    .expenseAmount(2000.00)
			    .approvalStatus(ApprovalStatus.APPROVED)
			    .applicationStatus(ApplicationStatus.SUBMITTED)
			    .storageId("ST003")
			    .expenseList(expList3)
			    .build();
		
		ExpenseItems expItems4 =  ExpenseItems.builder()
				.expenseItemName("Accommodation")
				.expenseItemCost(2000.00)
				.build();
		
		expItems4 =  expenseItemsRepo.save(expItems4);
		
		ExpenseList expList4 = ExpenseList.builder()
				.expenseItems(new ArrayList<>())
				.receivedTotalAmount(200.20)
				.build();
		expList4.getExpenseItems().add(expItems4);
		expList4= expenseListRepo.save(expList4);
		
		Expenses exp4 = Expenses.builder()
			    .expenseTitle("Training Fees")
			    .expenseDescription("Online professional development course")
			    .expenseDate(LocalDate.now().minusMonths(2))
			    .expenseAmount(1000.20)
			    .approvalStatus(ApprovalStatus.REJECTED)
			    .applicationStatus(ApplicationStatus.SUBMITTED)
			    .storageId("ST004")
			    .expenseList(expList4)
			    .build();
		
		
		
		employeeRepo.save(emp1);
		employeeRepo.save(emp2);
		employeeRepo.save(emp3);
		
		exp1.setEmployee(emp1);
		exp2.setEmployee(emp2);
		exp3.setEmployee(emp2);
		exp4.setEmployee(emp2);
		
		exp1 = expenseRepo.save(exp1);
		exp2 = expenseRepo.save(exp2);
		exp3 = expenseRepo.save(exp3);
		exp4 = expenseRepo.save(exp4);
		
		
		
		 emp1.getExpenses().add(exp1);
		 emp2.getExpenses().add(exp2);
		 emp2.getExpenses().add(exp3);
		 emp2.getExpenses().add(exp4);
		 
		 employeeRepo.save(emp1);
		 employeeRepo.save(emp2);

		
		
		PersistenceUtil pu = Persistence.getPersistenceUtil();
		System.out.println(pu.isLoaded(employeeRepo.findById(emp1.getEmployeeId()), "Employee List"));
		
		
	}

}