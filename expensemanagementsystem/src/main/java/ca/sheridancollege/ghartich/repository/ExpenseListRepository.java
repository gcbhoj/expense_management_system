package ca.sheridancollege.ghartich.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ghartich.beans.ExpenseList;

public interface ExpenseListRepository extends JpaRepository <ExpenseList,Long> {

}
