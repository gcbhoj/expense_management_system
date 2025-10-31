package ca.sheridancollege.ghartich.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ghartich.beans.ExpenseItems;

public interface ExpenseItemsRepository extends JpaRepository <ExpenseItems,Long> {

}
