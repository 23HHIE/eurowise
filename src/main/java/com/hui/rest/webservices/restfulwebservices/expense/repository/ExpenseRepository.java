package com.hui.rest.webservices.restfulwebservices.expense.repository;

import com.hui.rest.webservices.restfulwebservices.expense.ExpenditureList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenditureList, Integer> {

    List<ExpenditureList>  findByUsername(String username);

    List<ExpenditureList> findByUsernameAndTypeContains(String username, String type);

    //ExpenditureList addExpend(String username, String type, double fee, LocalDate date);
}
