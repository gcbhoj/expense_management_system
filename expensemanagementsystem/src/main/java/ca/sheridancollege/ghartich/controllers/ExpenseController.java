package ca.sheridancollege.ghartich.controllers;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sheridancollege.ghartich.beans.Employee;
import ca.sheridancollege.ghartich.beans.EmployeeRole;
import ca.sheridancollege.ghartich.beans.Expenses;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api/v1/expenses"})
@AllArgsConstructor
public class ExpenseController {

	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	private final String PYTHON_SERVICES = "http://localhost:5001/api/py/";
	
	
    @PostMapping(value = "/claimExpenses/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> claimNewExpense(
            @RequestPart("file") MultipartFile file,
            @RequestPart("expense") String expenseJson, // JSON as string
            @PathVariable Long employeeId
    ) throws Exception {
    	try {
    		
    		// using object mapping to map received json file with Expenses bean
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules(); // registers JavaTimeModule for LocalDate
            Expenses expense = mapper.readValue(expenseJson, Expenses.class);
            
            // using employee id to find employee from repository
            Optional <Employee> optionalEmployee = employeeRepo.findById(employeeId);
            
            //if employee not found
            if(optionalEmployee.isEmpty()) {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND)
        				.body(Map.of("status",404,
        						"message","Employee Not Found"));
            }
            
            // checking if the receipt upload is not null
            if(file.isEmpty()) {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        				.body(Map.of("status",400,
        						"message","Receipt Not Uploaded"));
            }
            
            // extracting the file name from the attachment            
            String originalFileName = file.getOriginalFilename();
            
            // checking for supported formats
            boolean isImage = imageFile(originalFileName);
            boolean isPDF = pdfFile(originalFileName);
            
            // returning if not supported format
            if(!isImage && !isPDF) {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        				.body(Map.of("status",400,
        						"message","Uploaded file not supported"));            	
            }
            
            //Retrieving employee information
            Employee employee = optionalEmployee.get();
            
            //checking for role integrity **ALL EMPLOYEES CANNOT CLAIM EXPENSES**            
            if(employee.getRole().equals(EmployeeRole.EMPLOYEE)) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN)
            			.body(Map.of("status",409,"message","Employee is not Authorized"));
            }
            
            // Null values check for expense input types
            if(expense.getExpenseTitle() == null || expense.getExpenseTitle().isBlank()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",403,
								"message","Expense type cannot be empty"));
				
			}
			if(expense.getExpenseDescription() == null || expense.getExpenseDescription().isBlank()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",403,
								"message","Expense Description cannot be empty"));
			}
			
			if(expense.getExpenseDate() ==  null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",400,
								"message","Expense Date cannot be empty"));
			}
			
			if(expense.getExpenseDate().isEqual(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",400,
								"message","Expense Date can"));				
			}
			
			if(expense.getExpenseAmount() <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",403,
								"message","Please Enter a valid Amount"));				
			}
			
			// use try catch method to call python API to extract receipt details
			
			try {
				
			}catch(Exception e) {
				e.printStackTrace();
	    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("status",500,
						"message","Please try again Later."));
				
			}
			
    
            



            return ResponseEntity.status(HttpStatus.CREATED)
    				.body(Map.of("status",200,
    						"message","Expense Saved Sucessfully","employee",employee));
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Map.of("status",500,
					"message","Please try again Later."));
    		
    	}

    }

	
	@GetMapping
	public List<Employee> getAllEmployee(){
		
		return employeeRepo.findAll();
	}
	/*
	 * Saving new expenses to employee
	 * */
//	@PostMapping(consumes = "application/json", value = "{employeeId}")
	@PostMapping(consumes = "multipart/form-data", value = "{employeeId}")
	public ResponseEntity<?> saveNewExpense(
			@RequestPart("expense") Expenses expenses,
			@RequestPart("file") MultipartFile file,
			@PathVariable Long employeeId) {
		try {
			Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
			
			if(optionalEmployee.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("status",404,
								"message","Employee Not Found"));
				
			}
			if(file.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",404,"message","Receipt is not uploaded"));
			}
			
			String originalFileName = file.getOriginalFilename();
			boolean isImage = imageFile(originalFileName);
			boolean isPDF = imageFile(originalFileName);
			
			if(!isImage && ! isPDF) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("stauts",403,"message","Uploaded file not supported. Upload either Jpg or PDF file."));
			}
			
			
			Employee employee = optionalEmployee.get();
			
//			if(employee.getRole() ==  EmployeeRole.EMPLOYEE) {
//				return ResponseEntity.status(HttpStatus.FORBIDDEN)
//						.body(Map.of("status",403,
//								"message","Employee Does not have permission to claim Expenses"));
//			}
			
			// checking for null values in expenses
			
			if(expenses.getExpenseTitle() == null || expenses.getExpenseTitle().isBlank()) {
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
						.body(Map.of("status",400,
								"message","Expense Date cannot be empty"));
			}
			
			if(expenses.getExpenseDate().isEqual(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",400,
								"message","Expense Date can"));
				
			}
			
			if(expenses.getExpenseAmount() <= 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("status",403,
								"message","Please Enter a valid Amount"));
				
			}
		

			
			
			
			// calling python API ENDPOINT to read the recepit and print the total amount
			 double pyExpenseAmount = 0;
			try {
			
			RestTemplate restTemplate= new RestTemplate();
			String result = restTemplate.getForObject(PYTHON_SERVICES+"read_receipt_jpg", String.class);
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
			
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("status",500,"message","Please try again later."));
		}
		
		
		
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
	private boolean pdfFile(String str) {
	    // Regex to check valid PDF file extension
	    String regex = "([^\\s]+(\\.(?i)(pdf))$)";

	    // Compile the regex
	    Pattern p = Pattern.compile(regex);

	    // If the string is null, return false
	    if (str == null) {
	        return false;
	    }

	    // Match the string against the regex
	    Matcher m = p.matcher(str);

	    // Return true if it matches
	    return m.matches();
	}

	
	



}
