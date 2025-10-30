package ca.sheridancollege.ghartich.controllers;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sheridancollege.ghartich.beans.Employee;
import ca.sheridancollege.ghartich.beans.Expenses;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api/v1/expenses","/api/v1/expenses/"})
@AllArgsConstructor
public class ExpenseController {
	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	private final String PYTHON_SERVICES = "http://localhost:5001/api/py/read_receipt";
	
	@GetMapping
	public List<Employee> getAllEmployee(){
		
		return employeeRepo.findAll();
	}
	/*
	 * Saving new expenses to employee
	 * */
	@PostMapping(consumes = "application/json", value = "{employeeId}")
	public ResponseEntity<?> saveNewExpense(@RequestBody Expenses expenses,@PathVariable Long employeeId) {

		Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
		
		if(optionalEmployee.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status",404,
							"message","Employee Not Found"));
			
		}
		
		Employee employee = optionalEmployee.get();
		
//		if(employee.getRole() ==  Role.EMPLOYEE) {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN)
//					.body(Map.of("status",403,
//							"message","Employee Does not have permission to claim Expenses"));
//		}
		
		// checking for null values in expenses
		
		if(expenses.getExpenseType() == null || expenses.getExpenseType().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status",403,
							"message","Expense type cannot be empty"));
			
		}
		if(expenses.getExpenseDescription() == null || expenses.getExpenseDescription().isBlank()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status",403,
							"message","Expense Description cannot be empty"));
		}
		
		if(expenses.getExpenseDate() ==  null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status",403,
							"message","Expense Date cannot be empty"));
		}
		
		if(expenses.getExpenseDate().isEqual(LocalDate.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status",403,
							"message","Expense Date can"));
			
		}
		
		if(expenses.getExpenseAmount() <= 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("status",403,
							"message","Please Enter a valid Amount"));
			
		}
		
		// To Do Add checks to verify if the upload file is in .jpeg or .png or .pdf format
		// calling python API ENDPOINT to read the recepit and print the total amount
		 double pyExpenseAmount = 0;
		try {
		
		RestTemplate restTemplate= new RestTemplate();
		String result = restTemplate.getForObject(PYTHON_SERVICES, String.class);
		System.out.println(result);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(result);
		pyExpenseAmount = node.path("total_amount").asDouble(0.0);
		System.out.println(pyExpenseAmount);
		
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status",500,
							"message","Error Calling Python service"));
			
		}
		if(expenses.getExpenseAmount() != pyExpenseAmount) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("status",409,
							"message","Entered Amount and the Receipt Amount Does not Match"));
			
		}
		
		// receive response from python and move forward with saving the expenses
		
		
		expenses = expenseRepo.save(expenses);
		
		employee.getExpenses().add(expenses);
		
		employeeRepo.save(employee);
		
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Map.of("status",200,
						"message","Expense Saved Sucessfully", 
						"employeeId",employee.getEmployeeId()));
		

		
		
		
		
	}
	
	private boolean imageFile(String str) {
		 // Regex to check valid image file extension.
        String regex
            = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
		
	}
	
	



}
