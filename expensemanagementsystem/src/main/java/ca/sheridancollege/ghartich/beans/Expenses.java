package ca.sheridancollege.ghartich.beans;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Expenses {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseId;
	@NonNull
	private String expenseType;
	@NonNull
	private String expenseDescription;
	@NonNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate expenseDate;
	@NonNull
	private double expenseAmount;
//	@NonNull
	private ApprovalStatus approvalStatus;
//	@NonNull
	private String storageId;
	
	private Date approvedDate;
	private String approvedBy;
	
	
	

}
