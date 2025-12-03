package com.budgettracker.DAO;

import com.budgettracker.models.RecurringTransaction;
import com.budgettracker.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecurringTransactionDAO {
    //create recurring transaction
    public void createRecurringTransaction(RecurringTransaction recurringTransaction) throws SQLException {
        String sql = "INSERT INTO recurring_transaction (amount, s_date, e_date, is_active, frequency, type, description, uid, cid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setBigDecimal(1, recurringTransaction.getAmount());
            statement.setDate(2, recurringTransaction.getS_date());
            statement.setDate(3, recurringTransaction.getE_date());
            statement.setBoolean(4, recurringTransaction.getIs_active());
            statement.setString(5, recurringTransaction.getFrequency());
            statement.setString(6, recurringTransaction.getType());
            statement.setString(7, recurringTransaction.getDescription());
            statement.setInt(8, recurringTransaction.getUid());
            statement.setInt(9, recurringTransaction.getCid());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                recurringTransaction.setRid(resultSet.getInt(1));
            }
        }
    }

    //get recurring transaction by id
    public RecurringTransaction getRecurringTransaction(int rid) throws SQLException {
        String sql = "SELECT * FROM recurring_transaction WHERE rid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, rid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                RecurringTransaction recurringTransaction = new RecurringTransaction();

                recurringTransaction.setRid(resultSet.getInt("rid"));
                recurringTransaction.setAmount(resultSet.getBigDecimal("amount"));
                recurringTransaction.setS_date(resultSet.getDate("s_date"));
                recurringTransaction.setE_date(resultSet.getDate("e_date"));
                recurringTransaction.setIs_active(resultSet.getBoolean("is_active"));
                recurringTransaction.setFrequency(resultSet.getString("frequency"));
                recurringTransaction.setType(resultSet.getString("type"));
                recurringTransaction.setDescription(resultSet.getString("description"));
                recurringTransaction.setUid(resultSet.getInt("uid"));
                recurringTransaction.setCid(resultSet.getInt("cid"));

                return recurringTransaction;
            }
        }

        return null;
    }

    //get all recurring transaction of user
    public List<RecurringTransaction> getAllRecurringTransactions(int uid) throws SQLException {
        String sql = "SELECT * FROM recurring_transaction WHERE uid = ?";
        List<RecurringTransaction> recurringTransactions = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RecurringTransaction recurringTransaction = new RecurringTransaction();

                recurringTransaction.setRid(resultSet.getInt("rid"));
                recurringTransaction.setAmount(resultSet.getBigDecimal("amount"));
                recurringTransaction.setS_date(resultSet.getDate("s_date"));
                recurringTransaction.setE_date(resultSet.getDate("e_date"));
                recurringTransaction.setIs_active(resultSet.getBoolean("is_active"));
                recurringTransaction.setFrequency(resultSet.getString("frequency"));
                recurringTransaction.setType(resultSet.getString("type"));
                recurringTransaction.setDescription(resultSet.getString("description"));
                recurringTransaction.setUid(resultSet.getInt("uid"));
                recurringTransaction.setCid(resultSet.getInt("cid"));

                recurringTransactions.add(recurringTransaction);
            }
            return recurringTransactions;
        }

    }

    //get active recurring transaction of user
    public List<RecurringTransaction> getAllActiveRecurringTransactions(int uid) throws SQLException {
        String sql = "SELECT * FROM recurring_transaction WHERE uid = ? AND is_active = true";
        List<RecurringTransaction> recurringTransactions = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RecurringTransaction recurringTransaction = new RecurringTransaction();
                recurringTransaction.setRid(resultSet.getInt("rid"));
                recurringTransaction.setAmount(resultSet.getBigDecimal("amount"));
                recurringTransaction.setS_date(resultSet.getDate("s_date"));
                recurringTransaction.setE_date(resultSet.getDate("e_date"));
                recurringTransaction.setIs_active(resultSet.getBoolean("is_active"));
                recurringTransaction.setFrequency(resultSet.getString("frequency"));
                recurringTransaction.setType(resultSet.getString("type"));
                recurringTransaction.setDescription(resultSet.getString("description"));
                recurringTransaction.setUid(resultSet.getInt("uid"));
                recurringTransaction.setCid(resultSet.getInt("cid"));

                recurringTransactions.add(recurringTransaction);
            }
            return recurringTransactions;
        }

    }

    //update recurring transactions
    public int updateRecurringTransaction(RecurringTransaction recurringTransaction) throws SQLException {
        String sql = "UPDATE recurring_transaction SET amount = ?, s_date = ?, e_date = ?, is_active = ?, frequency = ?, type = ?, description = ?, uid = ?, cid = ? WHERE rid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBigDecimal(1, recurringTransaction.getAmount());
            statement.setDate(2, recurringTransaction.getS_date());
            statement.setDate(3, recurringTransaction.getE_date());
            statement.setBoolean(4, recurringTransaction.getIs_active());
            statement.setString(5, recurringTransaction.getFrequency());
            statement.setString(6, recurringTransaction.getType());
            statement.setString(7, recurringTransaction.getDescription());
            statement.setInt(8, recurringTransaction.getUid());
            statement.setInt(9, recurringTransaction.getCid());
            statement.setInt(10, recurringTransaction.getRid());

            return  statement.executeUpdate();
        }
    }

    //change active status
    public int activeStatus(int rid, boolean is_active) throws SQLException {
        String sql = "UPDATE recurring_transaction SET is_active = ? WHERE rid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBoolean(1, is_active);
            statement.setInt(2, rid);

            return  statement.executeUpdate();
        }
    }

    //delete recurring transaction
    public boolean deleteRecurringTransaction(int rid) throws SQLException {
        String sql = "DELETE FROM recurring_transaction WHERE rid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, rid);
            return  statement.executeUpdate() > 0;
        }
    }
}
