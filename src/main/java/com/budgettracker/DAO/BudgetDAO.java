package com.budgettracker.DAO;

import com.budgettracker.models.Budget;
import com.budgettracker.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {
    //create new budget
    public void createBudget(Budget budget) throws SQLException {
        String sql = "INSERT INTO budget (limit_amount, year, month, uid, cid) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            pstmt.setBigDecimal(1, budget.getLimit_amount());
            pstmt.setInt(2, budget.getYear());
            pstmt.setInt(3, budget.getMonth());
            pstmt.setInt(4, budget.getUid());
            pstmt.setInt(5, budget.getCid());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                budget.setBid(rs.getInt(1));
            }
        }
    }

    //get budget by id
    public Budget getBudgetByID(int bid) throws SQLException {
        String sql = "SELECT * FROM budget WHERE bid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Budget budget = new Budget();

                budget.setBid(rs.getInt("bid"));
                budget.setLimit_amount(rs.getBigDecimal("limit_amount"));
                budget.setYear(rs.getInt("year"));
                budget.setMonth(rs.getInt("month"));
                budget.setUid(rs.getInt("uid"));
                budget.setCid(rs.getInt("cid"));
                return budget;
            }
        }

        return null;
    }

    //get all budget of user
    public List<Budget> getBudgetByUser(int uid) throws SQLException {
        String sql = "SELECT * FROM budget WHERE uid = ?";
        List<Budget> budgets = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Budget budget = new Budget();

                budget.setBid(rs.getInt("bid"));
                budget.setLimit_amount(rs.getBigDecimal("limit_amount"));
                budget.setYear(rs.getInt("year"));
                budget.setMonth(rs.getInt("month"));
                budget.setUid(rs.getInt("uid"));
                budget.setCid(rs.getInt("cid"));

                budgets.add(budget);
            }
            return budgets;
        }
    }

    //get budget from user by category, month, and year
    public Budget getBudgetSpecified(int uid, int cid, int month, int year) throws SQLException {
        String sql = "SELECT * FROM budget WHERE uid = ? AND cid = ? AND month = ? AND year = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            pstmt.setInt(2, cid);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                final Budget budget = new Budget();

                budget.setBid(rs.getInt("bid"));
                budget.setLimit_amount(rs.getBigDecimal("limit_amount"));
                budget.setYear(rs.getInt("year"));
                budget.setMonth(rs.getInt("month"));
                budget.setUid(rs.getInt("uid"));
                budget.setCid(rs.getInt("cid"));

                return budget;
            }
        }
        return null;
    }

    //update budget
    public int updateBudget(Budget budget) throws SQLException {
        String sql = "UPDATE budget SET limit_amount = ?, year = ?, month = ?, uid = ?, cid = ? WHERE bid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, budget.getLimit_amount());
            pstmt.setInt(2, budget.getYear());
            pstmt.setInt(3, budget.getMonth());
            pstmt.setInt(4, budget.getUid());
            pstmt.setInt(5, budget.getCid());
            pstmt.setInt(6, budget.getBid());

            return pstmt.executeUpdate();
        }
    }

    //delete budget
    public boolean deleteBudget(int bid) throws SQLException {
        String sql = "DELETE FROM budget WHERE bid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bid);
            return pstmt.executeUpdate() > 0;
        }
    }
}
