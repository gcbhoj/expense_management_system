package ca.sheridancollege.ghartich.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ghartich.beans.Employee;


public interface EmployeeRepository extends JpaRepository <Employee,Long> {
	
	
}
 