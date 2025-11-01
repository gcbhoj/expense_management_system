package ca.sheridancollege.ghartich.beans;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	private double expenseAmount;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
	
//	@NonNull
	private String storageId;
	
	@NonNull
	@OneToOne
	@JoinColumn(name = "expense_list_id") 
	private ExpenseList expenseList;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus = ApplicationStatus.SAVED;
	
	private Date approvedDate;
	private String approvedBy;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_Id")
    @JsonBackReference
    private Employee employee;
	
	
	

}
