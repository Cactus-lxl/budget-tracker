package com.budgettracker.DAO;

import com.budgettracker.models.Transaction;
import com.budgettracker.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    //create new transaction
    public void createNewTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (description, amount, time, type, uid, cid) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, transaction.getDescription());
            statement.setBigDecimal(2, transaction.getAmount());
            statement.setTimestamp(3, transaction.getTime());
            statement.setString(4, transaction.getType());
            statement.setInt(5, transaction.getUid());
            statement.setInt(6, transaction.getCid());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                transaction.setTid(resultSet.getInt(1));
            }
        }
    }

    //get transaction by tid
    public Transaction getTransaction(int tid) throws SQLException{
        String sql = "SELECT * FROM transaction WHERE tid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, tid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setTid(resultSet.getInt("tid"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setAmount(resultSet.getBigDecimal("amount"));
                transaction.setTime(resultSet.getTimestamp("time"));
                transaction.setType(resultSet.getString("type"));
                transaction.setUid(resultSet.getInt("uid"));
                transaction.setCid(resultSet.getInt("cid"));

                return transaction;
            }
        }
        return null;
    }

    //get all transactions of user
    public List<Transaction> getALLTransactions(int uid) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE uid = ?";
        List<Transaction> transactions = new ArrayList<Transaction>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setTid(resultSet.getInt("tid"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setAmount(resultSet.getBigDecimal("amount"));
                transaction.setTime(resultSet.getTimestamp("time"));
                transaction.setType(resultSet.getString("type"));
                transaction.setUid(resultSet.getInt("uid"));
                transaction.setCid(resultSet.getInt("cid"));

                transactions.add(transaction);
            }
            return transactions;
        }
    }

    //filter transaction of user by type
    public List<Transaction> getAllTransactionsOfUser(String type, int uid) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE type = ? AND uid = ?";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            statement.setInt(2, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setTid(resultSet.getInt("tid"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setAmount(resultSet.getBigDecimal("amount"));
                transaction.setTime(resultSet.getTimestamp("time"));
                transaction.setType(resultSet.getString("type"));
                transaction.setUid(resultSet.getInt("uid"));
                transaction.setCid(resultSet.getInt("cid"));

                transactions.add(transaction);
            }
            return transactions;
        }
    }

    //filter transaction of user by category name(cid)
    public List<Transaction> getAllTransactionsOfUserByCategory(int cid, int uid) throws SQLException {
        String sql = "SELECT * FROM transaction WHERE cid = ? AND uid = ?";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cid);
            statement.setInt(2, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();

                transaction.setTid(resultSet.getInt("tid"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setAmount(resultSet.getBigDecimal("amount"));
                transaction.setTime(resultSet.getTimestamp("time"));
                transaction.setType(resultSet.getString("type"));
                transaction.setUid(resultSet.getInt("uid"));
                transaction.setCid(resultSet.getInt("cid"));

                transactions.add(transaction);
            }
            return transactions;
        }
    }

    //update transaction
    public int updateTransaction(Transaction transaction) throws SQLException{
        String sql = "UPDATE transaction SET description = ?, amount = ?, time = ?, type = ?, uid = ?, cid= ? WHERE tid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getDescription());
            statement.setBigDecimal(2, transaction.getAmount());
            statement.setTimestamp(3, transaction.getTime());
            statement.setString(4, transaction.getType());
            statement.setInt(5, transaction.getUid());
            statement.setInt(6, transaction.getCid());
            statement.setInt(7, transaction.getTid());

            return statement.executeUpdate();
        }
    }

    //delete transaction
    public void deleteTransaction(int tid) throws SQLException{
        String sql = "DELETE FROM transaction WHERE tid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, tid);

            statement.executeUpdate();
        }
    }
}
