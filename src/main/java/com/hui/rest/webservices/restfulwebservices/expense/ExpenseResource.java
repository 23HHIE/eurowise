package com.hui.rest.webservices.restfulwebservices.expense;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
public class ExpenseResource {

    private ExpenditureListService expenseService;

    public ExpenseResource (ExpenditureListService expenseService){
        this.expenseService=expenseService;
    }

    //@GetMapping("/users/{username}/expenses")
    //public List<ExpenditureList> retrieveExpense(@PathVariable String username){
    //    return expenseService.findByUsername(username);
    //}

    @GetMapping("/{type}/expenses")
    public List<ExpenditureList> retrieveExpense(@PathVariable String type){
        return expenseService.findByType(type);
    }

    //@GetMapping("/all-expenses")
    //public List<ExpenditureList> retrieveAllExpense(@PathVariable String type){
    //    return expenseService.findByType(type);
    //}

    @GetMapping("/users/{username}/expenses")
    public List<ExpenditureList> retrieveExpenses(@PathVariable String username){
        return expenseService.findByUsername(username);
    }

    @GetMapping("/users/{username}/expenses/{id}")
    public ExpenditureList retrieveExpenses(@PathVariable String username, @PathVariable int id){
        return expenseService.findById(id);
    }

    @DeleteMapping("/users/{username}/expenses/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable String username, @PathVariable int id){
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/expenses/{id}")
    public ExpenditureList updateExpenses(@PathVariable String username,
                                               @PathVariable int id,
                                               @RequestBody ExpenditureList expense) {
        expenseService.updateExpense(expense);
        return expense;
    }

    @PostMapping("/users/{username}/expenses")
    public ExpenditureList createExpenses(@PathVariable String username,
                                          @RequestBody ExpenditureList expense) {
        ExpenditureList createdExpense = expenseService.addExpend(username, expense.getType(), expense.getFee(), expense.getDate());
        return createdExpense;
    }

}
