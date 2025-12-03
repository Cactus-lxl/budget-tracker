package com.budgettracker.DAO;

import com.budgettracker.models.Notification;
import com.budgettracker.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    //create notification
    public void createNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notification (message, is_read, create_date, uid, bid) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, notification.getMessage());
            statement.setBoolean(2, notification.isRead());
            statement.setTimestamp(3, notification.getCreateDate());
            statement.setInt(4, notification.getUid());
            statement.setInt(5, notification.getBid());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                notification.setNid(resultSet.getInt(1));
            }
        }
    }

    public Notification findNotificationByid(int nid) throws SQLException {
        String sql = "SELECT * FROM notification WHERE nid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Notification notification = new Notification();

                notification.setNid(resultSet.getInt("nid"));
                notification.setMessage(resultSet.getString("message"));
                notification.setIs_read(resultSet.getBoolean("is_read"));
                notification.setCreateDate(resultSet.getTimestamp("create_date"));
                notification.setUid(resultSet.getInt("uid"));
                notification.setBid(resultSet.getInt("bid"));

                return notification;
            }
        }
        return null;
    }

    //get all notification of user
    public List<Notification> findNotifications(int uid) throws SQLException {
        String sql = "SELECT * FROM notification WHERE uid = ?";
        List<Notification> notifications = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Notification  notification = new Notification();

                notification.setNid(resultSet.getInt("nid"));
                notification.setMessage(resultSet.getString("message"));
                notification.setIs_read(resultSet.getBoolean("is_read"));
                notification.setCreateDate(resultSet.getTimestamp("create_date"));
                notification.setUid(resultSet.getInt("uid"));
                notification.setBid(resultSet.getInt("bid"));

                notifications.add(notification);
            }
            return notifications;
        }
    }

    //get all unread notification of user
    public List<Notification> findAllNotifications(int uid) throws SQLException {
        String sql = "SELECT * FROM notification WHERE uid = ? AND is_read = FALSE";
        List<Notification> notifications = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, uid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Notification  notification = new Notification();

                notification.setNid(resultSet.getInt("nid"));
                notification.setMessage(resultSet.getString("message"));
                notification.setIs_read(resultSet.getBoolean("is_read"));
                notification.setCreateDate(resultSet.getTimestamp("create_date"));
                notification.setUid(resultSet.getInt("uid"));
                notification.setBid(resultSet.getInt("bid"));

                notifications.add(notification);
            }
            return notifications;
        }
    }

    //update notification
    public int updateNotification(Notification notification) throws SQLException {
        String sql = "UPDATE notification SET message = ?, is_read = ?, create_date = ?, uid = ?, bid = ? WHERE nid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, notification.getMessage());
            statement.setBoolean(2, notification.isRead());
            statement.setTimestamp(3, notification.getCreateDate());
            statement.setInt(4, notification.getUid());
            statement.setInt(5, notification.getBid());
            statement.setInt(6, notification.getNid());

            return statement.executeUpdate();
        }
    }

    //mark as read
    public int updateRead(int nid) throws SQLException {
        String  sql = "UPDATE notification SET is_read = TRUE WHERE nid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nid);
            return statement.executeUpdate();
        }
    }

    //delete notification
    public boolean deleteNotification(int nid) throws SQLException {
        String sql = "DELETE FROM notification WHERE nid = ?";

        try(Connection connection = DatabaseConfig.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nid);
            return statement.executeUpdate() > 0;
        }
    }
}
