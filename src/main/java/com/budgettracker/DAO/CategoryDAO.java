package com.budgettracker.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.budgettracker.models.Category;
import com.budgettracker.config.DatabaseConfig;


public class CategoryDAO {
    //get all categories
    public List<Category> getAllCategories() throws SQLException{
        String sql = "SELECT * FROM category";
        List<Category> categories = new ArrayList<Category>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Category c = new Category();
                c.setCid(resultSet.getInt("cid"));
                c.setCname(resultSet.getString("cname"));
                c.setType(resultSet.getString("type"));
                categories.add(c);
            }
        }
        return categories;
    }

    //get category by type
    public List<Category> getCategories(String type) throws SQLException{
        String sql = "SELECT * FROM category WHERE type = ?" ;
        List<Category> categories = new ArrayList<>();

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Category c = new Category();
                c.setCid(resultSet.getInt("cid"));
                c.setCname(resultSet.getString("cname"));
                c.setType(resultSet.getString("type"));
                categories.add(c);
            }
        }
        return categories;
    }

    //get category by name
    public Category getCategory(String cname) throws SQLException{
        String sql = "SELECT * FROM category WHERE cname = ?";

        try(Connection connection = DatabaseConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){

            statement.setString(1, cname);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Category c = new Category();

                c.setCid(resultSet.getInt("cid"));
                c.setCname(resultSet.getString("cname"));
                c.setType(resultSet.getString("type"));

                return  c;
            }
        }
        return null;
    }
}
