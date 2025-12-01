//manage connections to database
package com.budgettracker.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/budget_tracker";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    //load postgres driver into memory
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e){
            System.out.println("Driver could not be loaded");
            e.printStackTrace();
        }
    }

    //connect to DB
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    //disconnect from DB
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try{
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    //test connection
    public static boolean testConnection() {
        try (Connection conn = getConnection()){
            //check if conn is object is created and not closed(open)
            return conn != null && !conn.isClosed();
        }catch(SQLException e){
            return false;
        }
    }
}
