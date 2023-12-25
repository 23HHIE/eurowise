package com.hui.rest.webservices.restfulwebservices.expense;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ExpenditureListService {
    private static List<ExpenditureList> expList = new ArrayList<>() ;
    private static int expendsCount = 0;
    static {
        expList.add(new ExpenditureList(
                ++expendsCount,"hui","transport",1.99, LocalDate.now()));
        expList.add(new ExpenditureList(
                ++expendsCount,"hui","grocery",25.88, LocalDate.now()));
        expList.add(new ExpenditureList(
                ++expendsCount,"hui","movie",9.99, LocalDate.now()));
    }



    public List<ExpenditureList> findByUsername(String username){
        Predicate<? super ExpenditureList> predicate =
                expense -> expense.getUsername().equalsIgnoreCase(username);
        return expList.stream().filter(predicate).toList();
    }
    public ExpenditureList findById(int id) {
        Predicate<? super ExpenditureList> predicate = expense -> expense.getId() == id;
        ExpenditureList expense = expList.stream().filter(predicate).findFirst().get();
        return expense;
    }
    public List<ExpenditureList> findByType(String type){
        List<ExpenditureList> result = new ArrayList<>();

        for (ExpenditureList exp : expList) {
            if (exp.getType().equals(type)) {
                result.add(exp);
            }
        }

        return result;

        //return expList;
    }

    public ExpenditureList addExpend(String username, String type, Double fee, LocalDate expendDate){
        ExpenditureList expend = new ExpenditureList(++expendsCount, username,type, fee, expendDate);
        expList.add(expend);
        return expend;
    }

    public void deleteById(int id) {
        Predicate<? super ExpenditureList> predicate = expense -> expense.getId() == id;
        expList.removeIf(predicate);
    }

    public void updateExpense(ExpenditureList expense) {
        deleteById(expense.getId());
        expList.add(expense);
    }
}
