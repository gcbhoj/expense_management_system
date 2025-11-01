package ca.sheridancollege.ghartich.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ghartich.beans.ApplicationStatus;
import ca.sheridancollege.ghartich.beans.ApprovalStatus;
import ca.sheridancollege.ghartich.beans.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses,Long> {
	
	List<Expenses> findByEmployeeEmployeeIdAndApprovalStatus(Long employeeId, ApprovalStatus approvalStatus);
	List<Expenses> findByEmployeeEmployeeIdAndApplicationStatus(Long employeeId, ApplicationStatus applicationStatus);

}
