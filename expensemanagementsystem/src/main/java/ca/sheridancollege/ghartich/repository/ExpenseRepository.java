package ca.sheridancollege.ghartich.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.ghartich.beans.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses,Long> {

}
