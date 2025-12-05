package com.budgettracker.services;

import com.budgettracker.DAO.UserDAO;
import com.budgettracker.config.DatabaseConfig;
import com.budgettracker.models.Users;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.math.BigDecimal;


public class UserService {
    private UserDAO userDAO;

    public  UserService() {
        this.userDAO = new UserDAO();
    }

    //register new user
    public Users registerUser(String uname, String fname, String lname, String email, String password) throws SQLException{
        //valid new user info
        if(uname == null || uname.trim().isEmpty()){
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        if(password == null || password.length() < 8){
            throw new IllegalArgumentException("Password must have at least 8 characters");
        }

        Users existingUser = userDAO.findUserByUsername(uname);
        if(existingUser != null){
            throw new IllegalArgumentException("User name already exists");
        }

        //create new user and encr password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Users newUser = new Users();
        newUser.setUname(uname);
        newUser.setFName(fname);
        newUser.setlName(lname);
        newUser.setEmail(email);
        newUser.setPw(hashedPassword);
        newUser.setBalance(BigDecimal.ZERO);

        userDAO.createNewUser(newUser);
        return newUser;
    }

    //login
    public Users login(String uname, String password) throws SQLException{
        if(uname == null || uname.trim().isEmpty()){
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        Users existingUser = userDAO.findUserByUsername(uname);
        if(existingUser == null){
            throw new IllegalArgumentException("User name could not be found");
        }

        String hashedPassword = existingUser.getPw();
        Boolean result = BCrypt.checkpw(password, hashedPassword);
        if(!result){
            throw new IllegalArgumentException("Invalid username or password");
        }

        return existingUser;
    }

    //get user by username
    public Users findUserByUsername(String uname) throws SQLException{
        Users existingUser = userDAO.findUserByUsername(uname);
        return existingUser;
    }

    //get user by id
    public Users findUserById(int uid) throws SQLException{
        Users existingUser = userDAO.findUserById(uid);
        return existingUser;
    }

    //update user info
    public void updateUser(Users user) throws SQLException{
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }

        Users existingUser = userDAO.findUserById(user.getUserId());
        if(existingUser == null){
            throw new IllegalArgumentException("User could not be found");
        }

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            userDAO.updateUser(user, conn);
            conn.commit();
        }catch (SQLException e){
            if(conn != null){
                conn.rollback();
                throw e;
            }
        }finally {
            if(conn != null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    //update password
    public void updatePassword(int uid, String oldPassword, String newPassword) throws SQLException{
        Users existingUser = userDAO.findUserById(uid);
        if(existingUser == null){
            throw new IllegalArgumentException("User could not be found");
        }

        String hashedPassword = existingUser.getPw();
        Boolean result = BCrypt.checkpw(oldPassword, hashedPassword);
        if(!result){
            throw new IllegalArgumentException("old password does not match");
        }

        Boolean isSame = BCrypt.checkpw(newPassword, hashedPassword);
        if(isSame){
            throw new IllegalArgumentException("new password should not be the same as old password");
        }

        if(newPassword == null || newPassword.length() < 8){
            throw new IllegalArgumentException("new password must have at least 8 characters");
        }

        String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        userDAO.updatePassword(uid, newHashedPassword);
    }

    //add to balance
    public void addToBalance (int uid, BigDecimal amount) throws SQLException{
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("amount cannot be null or less than zero");
        }

        Users existingUser = userDAO.findUserById(uid);
        if(existingUser == null){
            throw new IllegalArgumentException("User could not be found");
        }

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            userDAO.addToBalance(uid, amount, conn);

            conn.commit();
        }catch(SQLException e){
            if(conn != null){
                conn.rollback();
                throw e;
            }
        }finally {
            if(conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    //remove from balance
    public void reduceBalance (int uid, BigDecimal amount) throws SQLException{
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("amount cannot be null or less than zero");
        }

        Users existingUser = userDAO.findUserById(uid);
        if(existingUser == null){
            throw new IllegalArgumentException("User could not be found");
        }

        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            userDAO.reduceBalance(uid, amount, conn);

            conn.commit();

        }catch(SQLException e){
            if(conn != null){
                conn.rollback();
                throw e;
            }
        }finally {
            if(conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
