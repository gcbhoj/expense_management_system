package ca.sheridancollege.ghartich.beans;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Expenses{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseId;
	@NonNull
	private String expenseTitle;
	@NonNull
	private String expenseDescription;
	@NonNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate expenseDate;
	@NonNull
	private double expenseAmount;
	
	@Builder.Default
	private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
	
//	@NonNull
	private String storageId;
	
	@NonNull
	@OneToOne
	@JoinColumn(name = "expense_list_id") 
	private ExpenseList expenseList;
	
	@Builder.Default
	private ApplicationStatus applicationStatus = ApplicationStatus.SAVED;
	
	private Date approvedDate;
	private String approvedBy;
	
	
	

}
