package ca.sheridancollege.ghartich.controllers;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.ghartich.beans.Employee;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api/v1/expenses","/api/v1/expenses/"})
@AllArgsConstructor
public class ExpenseController {
	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	
	
	@GetMapping
	public List<Employee> getAllEmployee(){
		
		return employeeRepo.findAll();
	}
	
	



}
