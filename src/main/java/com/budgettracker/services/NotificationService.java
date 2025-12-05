package com.budgettracker.services;

import com.budgettracker.DAO.NotificationDAO;
import com.budgettracker.models.Budget;
import com.budgettracker.models.Notification;
import com.budgettracker.DAO.BudgetDAO;
import com.budgettracker.services.BudgetService;

import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.notificationDAO = new NotificationDAO();
    }

    //create notification
    public void createNotification(Notification notification) throws SQLException {
        if(notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new SQLException("Notification message cant be empty");
        }
        if(notification.getUid()<=0){
            throw new SQLException("Invalid user id");
        }
        notificationDAO.createNotification(notification);
    }

    //get notification
    public Notification findNotification(int nid) throws SQLException {
        Notification existingNotification = notificationDAO.findNotificationByid(nid);
        if (existingNotification == null) {
            throw new SQLException("Notification not found");
        }
        return existingNotification;
    }

    //get all notifications
    public List<Notification> findAllNotifications(int uid) throws SQLException {
        List<Notification> existingNotification = notificationDAO.findNotifications(uid);
        if (existingNotification == null) {
            throw new SQLException("No notification found for the user");
        }
        return existingNotification;
    }

    //get notifications that are not read
    public List<Notification> findUnreadNotifications(int uid) throws SQLException {
        List<Notification> existingNotification = notificationDAO.findUnreadNotifications(uid);
        if (existingNotification == null) {
            throw new SQLException("No unread notification found for the user");
        }
        return existingNotification;
    }

    //delete notification
    public void deleteNotification(int nid) throws SQLException {
        Notification existingNotification = notificationDAO.findNotificationByid(nid);
        if (existingNotification == null) {
            throw new SQLException("Notification not found");
        }
        notificationDAO.deleteNotification(nid);
    }

    //mark as read
    public void changeReadStatus(int nid) throws SQLException {
        Notification notification = notificationDAO.findNotificationByid(nid);
        if (notification == null) {
            throw new SQLException("Notification not found");
        }

        notificationDAO.updateRead(nid);
    }
}
