package com.budgettracker.services;

import com.budgettracker.DAO.CategoryDAO;
import com.budgettracker.DAO.NotificationDAO;
import com.budgettracker.DAO.UserDAO;
import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.Budget;
import com.budgettracker.models.Notification;
import com.budgettracker.models.Transaction;
import com.budgettracker.services.BudgetService;

import com.budgettracker.DAO.TransactionDAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class TransactionService {
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;
    private BudgetService budgetService;

    public TransactionService(){
        this.transactionDAO = new TransactionDAO();
        this.userDAO = new UserDAO();
        this.budgetService = new BudgetService();
    }

    //create new transaction
    public void createTransaction(Transaction transaction) throws SQLException {
        BigDecimal amount = transaction.getAmount();
        String type = transaction.getType();
        int uid = transaction.getUid();
        int cid = transaction.getCid();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Transaction amount must be positive");
        }
        if (!type.equals("income") && !type.equals("expense")) {
            throw new SQLException("Transaction type must be either income or expense");
        }
        if(uid <= 0){
            throw new SQLException("You have to add transaction as a user");
        }
        if(cid <= 0){
            throw new SQLException("You have to have a category for your transaction ");
        }

        java.sql.Timestamp time = new Timestamp(System.currentTimeMillis());

        transaction.setTime(time);

        Connection conn = null;
        try{
            //get connection
            conn = DatabaseConfig.getConnection();

            //start transaction
            conn.setAutoCommit(false);

            //create "transaction" within the transaction
            transactionDAO.createNewTransaction(transaction, conn);
            if(type.equals("income")) {
                userDAO.addToBalance(uid, amount, conn);
            }
            else if(type.equals("expense")){
                userDAO.reduceBalance(uid, amount, conn);
            }

            conn.commit();
        }catch (SQLException e){
            if(conn != null){
                conn.rollback();
                throw e;
            }
        }finally{
            if(conn != null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        LocalDate transactionTime = transaction.getTime().toLocalDateTime().toLocalDate();
        int month = transactionTime.getMonthValue();
        int year = transactionTime.getYear();

        Budget budget = budgetService.getBudgetSpecified(uid, cid, month, year);

        if(budget != null){
            if(budgetService.exceedsBudget(uid, cid, month, year)){
                Notification notification = new Notification();
                notification.setUid(uid);
                notification.setBid(budget.getBid());
                notification.setMessage("Budget exceed");
                notification.setIs_read(false);
                notification.setCreateDate(new Timestamp(System.currentTimeMillis()));

                NotificationService notificationService = new NotificationService();
                notificationService.createNotification(notification);
            }
        }
    }

    //find a transaction
    public Transaction getTransactionById(int tid) throws SQLException{
        Transaction existingTransaction = transactionDAO.getTransaction(tid);
        if (existingTransaction == null) {
            throw new SQLException("Transaction not found");
        }

        return existingTransaction;
    }

    //find all transaction from user
    public List<Transaction> getTransactionsByUser(int uid) throws SQLException{
        List <Transaction> existingTransaction = transactionDAO.getALLTransactions(uid);

        if (existingTransaction == null) {
            throw new SQLException("Transactions not found");
        }

        return existingTransaction;
    }

    //filter all transaction from user by type
    public List<Transaction> getTransactionsByType(String type, int uid) throws SQLException{
        List <Transaction> existingTransaction = transactionDAO.getAllTransactionsOfUserByType(type, uid);
        if (existingTransaction == null) {
            throw new SQLException("Transactions not found");
        }

        return existingTransaction;
    }

    //filter all transaction from user by category
    public List<Transaction> getTransactionsByCategory(int cid, int uid) throws SQLException{
        List <Transaction> existingTransaction = transactionDAO.getAllTransactionsOfUserByCategory(cid, uid);
        if (existingTransaction == null) {
            throw new SQLException("Transactions not found");
        }

        return existingTransaction;
    }

    //update transaction
    public void updateTransaction(int tid, Transaction transaction) throws SQLException {
        Transaction existingTransaction = transactionDAO.getTransaction(tid);

        if (existingTransaction == null) {
            throw new SQLException("Transaction not found");
        }

        int uid = existingTransaction.getUid();
        BigDecimal amount = transaction.getAmount();
        String type = transaction.getType();

        //check for valid new transaction
        if (!type.equals("income") && !type.equals("expense")) {
            throw new SQLException("Transaction type must be either income or expense");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Transaction amount must be positive");
        }

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            //reverse the old transaction
            if(existingTransaction.getType().equals("expense")){
                userDAO.addToBalance(uid,existingTransaction.getAmount(), conn);
            }
            else if(existingTransaction.getType().equals("income")){
                userDAO.reduceBalance(uid, existingTransaction.getAmount(), conn);
            }

            existingTransaction.setTid(tid);
            transactionDAO.updateTransaction(transaction, conn);

            //add to new balance
            if(type.equals("expense")){
                userDAO.reduceBalance(uid, amount, conn);
            }
            else if(type.equals("income")){
                userDAO.addToBalance(uid, amount, conn);
            }

            conn.commit();

        } catch (SQLException e) {
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

        int cid = transaction.getCid();

        //convert timestamp to local date
        LocalDate transactionTime = transaction.getTime().toLocalDateTime().toLocalDate();
        int month = transactionTime.getMonthValue();
        int year = transactionTime.getYear();

        Budget budget = budgetService.getBudgetSpecified(uid, cid, month, year);
        if(budget != null){
            if(budgetService.exceedsBudget(uid, cid, month, year)){
                Notification notification = new Notification();
                notification.setUid(uid);
                notification.setBid(budget.getBid());
                notification.setMessage("Budget exceed");
                notification.setIs_read(false);
                notification.setCreateDate(new Timestamp(System.currentTimeMillis()));

                NotificationService notificationService = new NotificationService();
                notificationService.createNotification(notification);
            }
        }
    }

    //delete transaction
    public void deleteTransaction(int tid) throws SQLException{
        Transaction existingTransaction = transactionDAO.getTransaction(tid);
        int uid = existingTransaction.getUid();
        BigDecimal amount = existingTransaction.getAmount();
        String type = existingTransaction.getType();

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            if(type.equals("expense")){
                userDAO.addToBalance(uid, amount, conn);
            }
            else if(type.equals("income")){
                userDAO.reduceBalance(uid, amount, conn);
            }

            transactionDAO.deleteTransaction(tid, conn);

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
