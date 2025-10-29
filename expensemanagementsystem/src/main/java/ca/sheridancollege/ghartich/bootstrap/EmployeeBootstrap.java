package ca.sheridancollege.ghartich.bootstrap;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.sheridancollege.ghartich.beans.ApprovalStatus;
import ca.sheridancollege.ghartich.beans.Employee;
import ca.sheridancollege.ghartich.beans.Expenses;
import ca.sheridancollege.ghartich.beans.Role;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmployeeBootstrap implements CommandLineRunner {
	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	
	@Override
	public void run(String... arg) throws Exception{
		
//		Employee emp1 = new Employee (null,"John Doe",Role.EMPLOYEE,null);
//		Employee emp2 = new Employee(null,"Jack Rack",Role.ADMIN,null);
//		Employee emp3 = new Employee(null,"Jane Smith",Role.MANAGER,null);

		
		Employee emp1 = Employee.builder()
				.employeeName("John Doe")
				.role(Role.ADMIN)
				.expenses(new ArrayList<Expenses>())
				.build();
		Employee emp2 = Employee.builder()
				.employeeName("Jack Rack")
				.role(Role.MANAGER)
				.expenses(new ArrayList<Expenses>())
				.build();
		Employee emp3 = Employee.builder()
				.employeeName("Jane Smith")
				.role(Role.EMPLOYEE)
				.expenses(new ArrayList<Expenses>())
				.build();
		
		
		Expenses exp1 = Expenses.builder()
			    .expenseType("Office Supplies")
			    .expenseDescription("Printer ink and stationery")
			    .expenseDate(LocalDate.now().minusDays(1))
			    .expenseAmount(200.20)
			    .approvalStatus(ApprovalStatus.PENDING)
			    .storageId("ST001")
			    .build();
		Expenses exp2 = Expenses.builder()
			    .expenseType("Travel Expenses")
			    .expenseDescription("Flight to Ottawa for client meeting")
			    .expenseDate(LocalDate.now().minusMonths(1))
			    .expenseAmount(2000.20)
			    .approvalStatus(ApprovalStatus.PENDING)
			    .storageId("ST002")
			    .build();
		Expenses exp3 = Expenses.builder()
			    .expenseType("Accommodation")
			    .expenseDescription("Hotel stay for business trip")
			    .expenseDate(LocalDate.now().minusMonths(1))
			    .expenseAmount(2000.20)
			    .approvalStatus(ApprovalStatus.APPROVED)
			    .storageId("ST003")
			    .build();
		Expenses exp4 = Expenses.builder()
			    .expenseType("Training Fees")
			    .expenseDescription("Online professional development course")
			    .expenseDate(LocalDate.now().minusMonths(2))
			    .expenseAmount(2000.20)
			    .approvalStatus(ApprovalStatus.REJECTED)
			    .storageId("ST004")
			    .build();
		
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
		employeeRepo.save(emp3);
		
		
		PersistenceUtil pu = Persistence.getPersistenceUtil();
		System.out.println(pu.isLoaded(employeeRepo.findById(emp1.getEmployeeId()), "Employee List"));
		
		
	}

}