package com.budgettracker.DAO;

import com.budgettracker.models.Goal;
import com.budgettracker.config.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {
    //create goal
    public void createGoal(Goal goal) throws SQLException {
        String sql = "INSERT INTO goal (target, current, create_date, end_date, uid) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setBigDecimal(1, goal.getTarget());
            pstmt.setBigDecimal(2, goal.getCurrent());
            pstmt.setDate(3,goal.getCreate_date());
            pstmt.setDate(4,goal.getEnd_date());
            pstmt.setInt(5,goal.getUid());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                goal.setGid(rs.getInt(1));
            }
        }
    }

    //get goal by id
    public Goal getGoal(int gid) throws SQLException {
        String sql = "SELECT * FROM goal WHERE gid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, gid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Goal goal = new Goal();

                goal.setGid(rs.getInt("gid"));
                goal.setTarget(rs.getBigDecimal("target"));
                goal.setCurrent(rs.getBigDecimal("current"));
                goal.setCreate_date(rs.getDate("create_date"));
                goal.setEnd_date(rs.getDate("end_date"));

                goal.setUid(rs.getInt("uid"));
                return goal;
            }
        }
        return null;
    }

    //get goal of user
    public Goal getGoalByUser(int uid) throws SQLException {
        String sql = "SELECT * FROM goal WHERE uid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Goal goal = new Goal();

                goal.setGid(rs.getInt("gid"));
                goal.setTarget(rs.getBigDecimal("target"));
                goal.setCurrent(rs.getBigDecimal("current"));
                goal.setCreate_date(rs.getDate("create_date"));
                goal.setEnd_date(rs.getDate("end_date"));

                goal.setUid(rs.getInt("uid"));
                return goal;
            }
            return null;
        }
    }

    //update goal
    public int updateGoal(Goal goal) throws SQLException {
        try(Connection connection = DatabaseConfig.getConnection();){
            return updateGoal(goal, connection);
        }
    }

    public int updateGoal(Goal goal, Connection connection) throws SQLException {
        String sql = "UPDATE goal SET target = ?, current = ?, create_date = ?, end_date = ?, uid = ? WHERE gid = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, goal.getTarget());
            pstmt.setBigDecimal(2, goal.getCurrent());
            pstmt.setDate(3, goal.getCreate_date());
            pstmt.setDate(4, goal.getEnd_date());
            pstmt.setInt(5, goal.getUid());
            pstmt.setInt(6, goal.getGid());

            return pstmt.executeUpdate();
        }
    }

    //update current
    public void updateCurrent(int gid, BigDecimal amount, Connection conn) throws SQLException {
        String sql = "UPDATE goal SET current = current + ? WHERE gid = ?";

        try(PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, gid);

            statement.executeUpdate();
        }
    }

    //delete goal
    public boolean deleteGoal(int gid) throws SQLException {
        String sql = "DELETE FROM goal WHERE gid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, gid);
            return pstmt.executeUpdate() > 0;
        }
    }
}
