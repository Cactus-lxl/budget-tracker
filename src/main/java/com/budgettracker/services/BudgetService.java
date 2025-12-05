package com.budgettracker.services;

import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.DAO.BudgetDAO;
import com.budgettracker.models.Budget;
import com.budgettracker.models.Transaction;
import com.budgettracker.DAO.TransactionDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public class BudgetService {
    private BudgetDAO budgetDAO;
    private TransactionDAO transactionDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
        this.transactionDAO = new TransactionDAO();
    }

    //check duplicates
    public Budget getBudgetSpecified(int uid, int cid, int month, int year) throws SQLException {
        return budgetDAO.getBudgetSpecified(uid, cid, month, year);
    }

    //create budget
    public void createBudget(Budget budget) throws SQLException {
        BigDecimal limit = budget.getLimit_amount();
        int uid = budget.getUid();
        int cid = budget.getCid();
        int budgetMonth = budget.getMonth();
        int budgetYear = budget.getYear();

        int currentYear= LocalDate.now().getYear();
        int currentMonth= LocalDate.now().getMonthValue();

        if(limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("limit amount cannot be less than zero");
        }

        //valid month and year
        if(budgetMonth <1 || budgetMonth > 12){
            throw new SQLException("You cannot create a budget with a invalid month");
        }
        if(currentYear > budgetYear){
            throw new SQLException("You cannot create a budget for previous year");
        }
        if(currentMonth > budget.getMonth() && currentYear == budgetYear){
            throw new SQLException("You cannot create a budget for previous month");
        }

        Budget duplicateBudget = getBudgetSpecified(uid, cid, budgetMonth, budgetYear);
        if(duplicateBudget != null) {
            throw new SQLException("Already existing a budget in the selected category and time");
        }

        budgetDAO.createBudget(budget);
    }

    //get budget
    public Budget getBudget(int bid) throws SQLException {
        Budget existingBudget = budgetDAO.getBudgetByID(bid);
        if (existingBudget == null) {
            throw new SQLException("Budget not found");
        }

        return existingBudget;
    }

    //get all budget
    public List<Budget> getAllBudgets(int uid) throws SQLException {
        List<Budget> budgets = budgetDAO.getBudgetByUser(uid);

        if (budgets == null) {
            throw new SQLException("No budgets found");
        }

        return budgets;
    }

    //update budget
    public void updateBudget(Budget budget) throws SQLException {
        Budget existingBudget = budgetDAO.getBudgetByID(budget.getBid());
        if (existingBudget == null) {
            throw new SQLException("Budget not found");
        }

        int uid = budget.getUid();
        int cid = budget.getCid();
        int budgetYear= budget.getYear();
        int budgetMonth= budget.getMonth();

        Budget duplicateBudget = getBudgetSpecified(uid, cid, budgetMonth, budgetYear);

        if(duplicateBudget!= null && duplicateBudget.getBid() != budget.getBid()) {
            throw new SQLException("Already existing a budget in the selected category during the selected month and year");
        }

        budgetDAO.updateBudget(budget);
    }

    //delete budget
    public void deleteBudget(int bid) throws SQLException {
        Budget existingBudget = budgetDAO.getBudgetByID(bid);
        if (existingBudget == null) {
            throw new SQLException("Budget not found");
        }
        budgetDAO.deleteBudget(bid);
    }

    //check if transaction exceeds the budget
    public boolean exceedsBudget(int uid, int cid, int month, int year) throws SQLException {
        Budget existingBudget = budgetDAO.getBudgetSpecified(uid, cid, month, year);
        if (existingBudget == null) {return false;}

        List<Transaction> transactions = transactionDAO.getAllTransactionsOfUserAtParticularTime(uid, cid, month, year);
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal limit = existingBudget.getLimit_amount();

        for(int i=0; i<transactions.size(); i++){
            Transaction t = transactions.get(i);
            if(t.getType().equals("expense")){
                totalAmount = totalAmount.add(t.getAmount());
            }
        }

        //compareTo returns 1 if totalAmount is greater than limit
        if(totalAmount.compareTo(limit) > 0){
            return true;
        }
        return false;
    }

}
