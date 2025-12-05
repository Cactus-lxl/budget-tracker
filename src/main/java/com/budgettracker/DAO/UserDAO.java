package com.budgettracker.DAO;

import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.Users;
import java.math.BigDecimal;
import java.sql.*;

public class UserDAO {
    // create user
    public void createNewUser(Users user) throws SQLException {
        String sql = "INSERT INTO users (uname, fname, lname, email, pw, balance) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //set all parameters
            statement.setString(1, user.getUname());
            statement.setString(2, user.getFName());
            statement.setString(3, user.getlName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPw());
            statement.setBigDecimal(6, user.getBalance());

            //execute sql
            statement.executeUpdate();

            //Get the auto-generated Id
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                //set it back to the object
                user.setUserId(resultSet.getInt(1));
            }
        }
    }

    // find user by username
    public Users findUserByUsername(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE uname = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Users user = new Users();

                user.setUserId(resultSet.getInt("uid"));
                user.setUname(resultSet.getString("uname"));
                user.setFName(resultSet.getString("fname"));
                user.setlName(resultSet.getString("lname"));
                user.setEmail(resultSet.getString("email"));
                user.setPw(resultSet.getString("pw"));
                user.setBalance(resultSet.getBigDecimal("balance"));

                return user;
            }
        }
        return null;
    }

    // find user by user id
    public Users findUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE uid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Users user = new Users();

                user.setUserId(resultSet.getInt("uid"));
                user.setUname(resultSet.getString("uname"));
                user.setFName(resultSet.getString("fname"));
                user.setlName(resultSet.getString("lname"));
                user.setEmail(resultSet.getString("email"));
                user.setPw(resultSet.getString("pw"));
                user.setBalance(resultSet.getBigDecimal("balance"));

                return user;
            }
        }
        return null;
    }

    //update user with acid(roll back)
    public int updateUser(Users user, Connection connection) throws SQLException {
        String sql = "UPDATE users SET uname = ?, fname = ?,lname = ?,email = ?,pw = ?,balance = ? WHERE uid = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUname());
            statement.setString(2, user.getFName());
            statement.setString(3, user.getlName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPw());
            statement.setBigDecimal(6, user.getBalance());
            statement.setInt(7, user.getUserId());

            return statement.executeUpdate();
        }
    }

    //update Password
    public int updatePassword(int uid, String pw) throws SQLException {
        String sql = "UPDATE users SET pw = ? WHERE uid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pw);
            statement.setInt(2, uid);

            return statement.executeUpdate();
        }
    }

    // Delete user
    public boolean deleteUser(Users user) throws SQLException {
        String sql = "DELETE FROM users WHERE uid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getUserId());
            return statement.executeUpdate() > 0;
        }
    }

    // Add to user balance
    public void addToBalance(int uid, BigDecimal amount, Connection connection) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE uid = ?";
        
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, uid);

            statement.executeUpdate();
        }
    }

    // Subtract from user balance
    public void reduceBalance(int uid, BigDecimal amount, Connection connection) throws SQLException {
        String sql = "UPDATE users SET balance = balance - ? WHERE uid = ?";
        
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, uid);
            
            statement.executeUpdate();
        }
    }

    // Get current balance
    public BigDecimal getBalance(int uid) throws SQLException {
        String sql = "SELECT balance FROM users WHERE uid = ?";
        
        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, uid);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("balance");
            } else {
                throw new SQLException("User not found with id: " + uid);
            }
        }
    }
}
