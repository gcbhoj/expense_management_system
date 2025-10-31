package ca.sheridancollege.ghartich.controllers;


import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import ca.sheridancollege.ghartich.beans.ExpenseItems;
import ca.sheridancollege.ghartich.beans.ExpenseList;
import ca.sheridancollege.ghartich.beans.Expenses;
import ca.sheridancollege.ghartich.repository.EmployeeRepository;
import ca.sheridancollege.ghartich.repository.ExpenseItemsRepository;
import ca.sheridancollege.ghartich.repository.ExpenseListRepository;
import ca.sheridancollege.ghartich.repository.ExpenseRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api/v1/expenses"})
@AllArgsConstructor
public class ExpenseController {

	
	private final EmployeeRepository employeeRepo;
	private final ExpenseRepository expenseRepo;
	private final ExpenseListRepository expenseListRepo;
	private final ExpenseItemsRepository expenseItemsRepo;
	
	
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
			
			double extractedTotalAmount = 0;
			List<ExpenseItems> expenseItems = new ArrayList<>();
			
			
			// Creating a new Temp file to store the file instance
			File tempFile = File.createTempFile(employee.getEmployeeName(),"_"+ file.getOriginalFilename());
			file.transferTo(tempFile);
			
			try {
				
				// creating http headers
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				
				MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
				body.add("file", new FileSystemResource(tempFile));
				
				HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,headers);
				
				
				RestTemplate restTemplate = new RestTemplate();
				
				ResponseEntity<String> response;

				if (!isPDF) {
				    // For image files (JPG, PNG, etc.)
				    response = restTemplate.postForEntity(PYTHON_SERVICES + "read_receipt_jpg", requestEntity, String.class);
				} else {
				    // For PDF files
				    response = restTemplate.postForEntity(PYTHON_SERVICES + "read_receipt_pdf", requestEntity, String.class);
				}
				
	            // Handle response
	            if (response == null || response.getBody() == null) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                        .body(Map.of("status", 400, "message", "No response from Python service"));
	            }
	            
	            // unwrapping the response from python service
	            JsonNode jsonNode = mapper.readTree(response.getBody());
	            JsonNode receiptData = jsonNode.path("receipt_data"); // go into nested object

	            extractedTotalAmount = receiptData.path("total_amount").asDouble();
	            
	            // checking for null total from reading receipt
	            if(extractedTotalAmount <= 0) {
	            	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            			.body(Map.of("status",400,"message","Unable to retreive total amount from receipt"));
	            		            	
	            }
	            JsonNode items = receiptData.path("items");
	            
	            if(items.isEmpty()) {
	            	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            			.body(Map.of("status",400,"message","Unable to retreive receipt details from receipt"));
	            }
	            
	            // iterating through the items and adding to the expenseItems
	            for(JsonNode itemNode:items) {
	                ExpenseItems expenseItem = ExpenseItems.builder()
	                        .expenseItemName(itemNode.path("item").asText())
	                        .expenseItemCost(itemNode.path("amount").asDouble()) // make sure your POJO uses double
	                        .build();
	                expenseItems.add(expenseItem);
	            }			
			}catch(Exception e) {
				e.printStackTrace();
	    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("status",500,
						"message","Please try again Later."));			
			}
			
			// validating the user entered total expense amount and the total amount received from reading the receipt
			
			if(extractedTotalAmount != expense.getExpenseAmount()) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(Map.of("status", 400, "message", "Receipt Total & Entered Total Does Not Match"));
			}
			
			//saving expense item repo			
			expenseItemsRepo.saveAll(expenseItems);
			
			// Building Expense list			
			ExpenseList expenseList = ExpenseList.builder()
					.expenseItems(expenseItems)
					.receivedTotalAmount(extractedTotalAmount)
					.build();
			
			//saving expense list to repository
			
			expenseList = expenseListRepo.save(expenseList);
			
			//Linking expense to expense list
			expense.setExpenseList(expenseList);
			
			// saving expenses
			expense.setStorageId(tempFile.getName());
			expenseRepo.save(expense);
			
			//Linking expense to employee
			employee.getExpenses().add(expense);
			employeeRepo.save(employee);           

			// delteing the tempfile at the end
            tempFile.delete();

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
