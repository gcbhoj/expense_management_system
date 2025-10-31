package ca.sheridancollege.ghartich.beans;

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
public class ExpenseItems {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseItemId;
	@NonNull
	private String expenseItemName;
	
	private double expenseItemCost;
	

}
