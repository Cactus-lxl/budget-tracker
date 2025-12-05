package com.budgettracker.services;

import com.budgettracker.DAO.GoalDAO;
import com.budgettracker.DAO.UserDAO;
import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.Goal;
import com.budgettracker.models.Notification;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import static java.sql.Types.NULL;

public class GoalService {
    private GoalDAO goalDAO;
    private UserDAO userDAO;

    public GoalService() {
        this.goalDAO = new GoalDAO();
        this.userDAO = new UserDAO();
    }

    //create goal
    public void createGoal(Goal goal) throws SQLException {
        BigDecimal target = goal.getTarget();
        Date eDate = goal.getEnd_date();
        if (target == null || target.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Goal target cannot be less than zero");
        }

        Goal ifexistGoal = goalDAO.getGoalByUser(goal.getUid());
        if (ifexistGoal != null) {
            throw new SQLException("One goal at a time");
        }

        goalDAO.createGoal(goal);
    }

    //get goal by id
    public Goal getGoal(int gid) throws SQLException {
        Goal existingGal = goalDAO.getGoal(gid);
        if (existingGal == null) {
            throw new SQLException("Goal not found");
        }
        return existingGal;
    }

    //get goal of user
    public Goal getGoalOfUser(int uid) throws SQLException {
        Goal existingGal = goalDAO.getGoalByUser(uid);
        if (existingGal == null) {
            throw new SQLException("Goal not found");
        }
        return existingGal;
    }

    //update goal
    public void updateGoal(Goal goal) throws SQLException {
        Goal existingGal = goalDAO.getGoal(goal.getGid());
        if (existingGal == null) {
            throw new SQLException("Goal not found");
        }

        BigDecimal target = goal.getTarget();
        Date eDate = goal.getEnd_date();
        if (target == null || target.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Goal target cannot be less than zero");
        }

        goalDAO.updateGoal(goal);
    }

    //delete goal
    public void deleteGoal(int gid) throws SQLException {
        Goal existingGal = goalDAO.getGoal(gid);
        if (existingGal == null) {
            throw new SQLException("Goal not found");
        }
        goalDAO.deleteGoal(gid);
    }

    //transfer from balance to current progress
    public void transferToGoal(int gid, int uid) throws SQLException {
        Goal existingGal = goalDAO.getGoal(gid);
        if (existingGal == null) {
            throw new SQLException("Goal not found");
        }
        if(existingGal.getUid() != uid){
            throw new SQLException("Goal does not belong to user");
        }
        if(reached(goalDAO.getGoal(gid))){
            throw new SQLException("Goal already reached");
        }
        BigDecimal amount = userDAO.getBalance(uid);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SQLException("Account balance too low!! Cannot be transferred to the current goal status");
        }

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            userDAO.reduceBalance(uid, amount, conn);
            goalDAO.updateCurrent(gid, amount, conn);

            conn.commit();
        }catch(SQLException e){
            if(conn != null) conn.rollback();
            throw e;
        }finally{
            if(conn != null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        if(reached(goalDAO.getGoal(gid))){
            Goal goal = goalDAO.getGoal(gid);

            Notification notification = new Notification();
            notification.setUid(uid);
            notification.setBid(null);
            notification.setMessage("Goal reached!!! :)");
            notification.setIs_read(false);
            notification.setCreateDate(new Timestamp(System.currentTimeMillis()));

            NotificationService notificationService = new NotificationService();
            notificationService.createNotification(notification);
        }

    }

    //check if goal has been reached
    private boolean reached(Goal goal){
        return goal.getCurrent().compareTo(goal.getTarget()) >= 0;
    }
}
