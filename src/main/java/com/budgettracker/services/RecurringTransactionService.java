package com.budgettracker.services;

import com.budgettracker.DAO.RecurringTransactionDAO;
import com.budgettracker.DAO.TransactionDAO;
import com.budgettracker.DAO.UserDAO;
import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.RecurringTransaction;
import com.budgettracker.models.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class RecurringTransactionService {
    private RecurringTransactionDAO recurringTransactionDAO;
    private UserDAO userDAO;

    public RecurringTransactionService() {
        this.recurringTransactionDAO = new RecurringTransactionDAO();
        this.userDAO = new UserDAO();
    }

    //create recurring transaction
    public void createRecurringTransaction(RecurringTransaction recurringTransaction) throws SQLException {
        BigDecimal amount = recurringTransaction.getAmount();
        String type = recurringTransaction.getType();
        String frequency = recurringTransaction.getFrequency();

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new SQLException("Recurring transaction amount cannot be zero or negative");
        }
        if(!type.equalsIgnoreCase("income") && !type.equalsIgnoreCase("expense")){
            throw new SQLException("Recurring transaction type must be 'income' or 'expense'");
        }
        if(!frequency.equals("weekly") && !frequency.equals("monthly") && !frequency.equals("yearly")){
            throw new SQLException("Recurring transaction frequency must be 'weekly', 'monthly' or yearly");
        }

        recurringTransactionDAO.createRecurringTransaction(recurringTransaction);
    }

    //find recurring transaction
    public RecurringTransaction getRecurringTransaction(int rid) throws SQLException{
        RecurringTransaction existingTransaction = recurringTransactionDAO.getRecurringTransaction(rid);
        if (existingTransaction == null) {
            throw new SQLException("Recurring Transaction not found");
        }

        return existingTransaction;
    }

    //find all recurring transaction from user
    public List<RecurringTransaction> getTransactionsByUser(int uid) throws SQLException{
        List <RecurringTransaction> existingTransaction = recurringTransactionDAO.getAllRecurringTransactions(uid);

        if (existingTransaction == null) {
            throw new SQLException("Recurring Transactions not found");
        }

        return existingTransaction;
    }

    //filter all active recurring transaction from user
    public List<RecurringTransaction> getTransactionsByType(boolean is_active, int uid) throws SQLException{
        List <RecurringTransaction> existingTransaction = recurringTransactionDAO.getAllActiveRecurringTransactions(uid);
        if (existingTransaction == null) {
            throw new SQLException("Recurring Transactions not found");
        }

        return existingTransaction;
    }

    //update recurring transaction
    public void updateRecurringTransaction(int rid, RecurringTransaction recurringTransaction) throws SQLException {
        RecurringTransaction existingTransaction = recurringTransactionDAO.getRecurringTransaction(rid);

        if (existingTransaction == null) {
            throw new SQLException("Transaction not found");
        }

        BigDecimal amount = recurringTransaction.getAmount();
        String type = recurringTransaction.getType();

        //check for valid new transaction
        if (!type.equals("income") && !type.equals("expense")) {
            throw new SQLException("Transaction type must be either income or expense");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Transaction amount must be positive");
        }

        recurringTransaction.setRid(rid);
        recurringTransactionDAO.updateRecurringTransaction(recurringTransaction);
    }

    //delete recurring transaction
    public void deleteRecurringTransaction(int rid) throws SQLException{
        RecurringTransaction existingTransaction = recurringTransactionDAO.getRecurringTransaction(rid);

        if (existingTransaction == null) {
            throw new SQLException("Recurring Transaction not found");
        }

        recurringTransactionDAO.deleteRecurringTransaction(rid);
    }

    //check if recurring transaction should be processed
    private boolean shouldProcess(RecurringTransaction recurringTransaction, java.sql.Date today) {
        int rid = recurringTransaction.getRid();
        String frequency = recurringTransaction.getFrequency();
        Boolean is_active = recurringTransaction.getIs_active();
        if(!is_active) {
            return false;
        }

        //convert sql.date to local date
        LocalDate todayLocal = today.toLocalDate();
        LocalDate sDate = recurringTransaction.getS_date().toLocalDate();

        if(todayLocal.isBefore(sDate)){
            return false;
        }

        java.sql.Date eDate = recurringTransaction.getE_date();
        if(eDate != null){
            if(todayLocal.isAfter(eDate.toLocalDate())){
                try {
                    recurringTransactionDAO.activeStatus(rid, false);
                    return false;
                }catch (SQLException e){}
            }
        }

        //get last processed date
        java.sql.Date lastProcessed = recurringTransaction.getLast_processed_date();
        LocalDate lastProcessedDate = null;
        if(lastProcessed != null) {
            lastProcessedDate = lastProcessed.toLocalDate();
        }

        if(frequency.equals("weekly")){
            boolean isMonday = todayLocal.getDayOfWeek().equals(DayOfWeek.MONDAY);
            if(!isMonday){return false;}

            if(lastProcessedDate == null){return true;}

            //check if 7 days has passed
            return lastProcessedDate.isBefore(todayLocal.minusDays(6));
        }
        else if(frequency.equals("monthly")){
            int dayOfMonth = todayLocal.getDayOfMonth();
            if(dayOfMonth != 1){return false;}
            if(lastProcessedDate == null){return true;}

            boolean samemonth = (lastProcessedDate.getYear() == todayLocal.getYear() && lastProcessedDate.getMonth() == todayLocal.getMonth());

            //process the recurring transaction if it is a different month
            return !samemonth;
        }
        else if(frequency.equals("yearly")){
            boolean isJanuary1 = (todayLocal.getDayOfMonth() == 1 && todayLocal.getMonthValue() == 1);
            if(!isJanuary1){return false;}

            if(lastProcessedDate == null){return true;}

            boolean differentYear = lastProcessedDate.getYear() < todayLocal.getYear();
            return  differentYear;
        }

        return false;
    }

    //processes the recurring transaction
    public void processRecurringTransaction(int rid) throws SQLException{
        RecurringTransaction existingTransaction = recurringTransactionDAO.getRecurringTransaction(rid);
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        if(shouldProcess(existingTransaction, today) == true){
            Connection conn = null;
            BigDecimal amount = existingTransaction.getAmount();
            String type = existingTransaction.getType();
            int uid = existingTransaction.getUid();
            try{
                conn = DatabaseConfig.getConnection();
                conn.setAutoCommit(false);

                if(type.equals("income")){
                    userDAO.addToBalance(uid, amount, conn);
                }
                else if(type.equals("expense")){
                    userDAO.reduceBalance(uid, amount, conn);
                }

                recurringTransactionDAO.updateProcessedDate(rid, today, conn);

                conn.commit();
            }catch (SQLException e){
                if(conn != null){
                    conn.rollback();
                    throw e;
                }
            }finally{
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }
        }
    }
}
