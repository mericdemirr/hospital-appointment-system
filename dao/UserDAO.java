package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 * UserDAO handles authentication and administrative user management tasks.
 */  
public class UserDAO {

    // 1. LOGIN: Verifies credentials during the login process
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
     
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Return User object with role if authentication is successful
                String role = rs.getString("role");
                return new User(username, password, role);
            }
            
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null; // Return null if user not found or password is incorrect
    }

    // 2. ADD USER: Registers a new system user (Doctor, Secretary, or Admin)
    public boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";
        
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            
            pstmt.executeUpdate();
            return true; // Successfully registered
        } catch (SQLException e) {
            // This will trigger if the username (Primary Key) already exists
            System.out.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    // 3. DELETE USER: Removes a user account from the system
    public boolean deleteUser(String username) {
        // SAFETY CHECK: Prevent the deletion of the master 'admin' account
        if ("admin".equals(username)) {
            System.out.println("Security Alert: Primary administrator cannot be deleted.");
            return false;
        }

        String sql = "DELETE FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Return true if a user was actually deleted
            
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // 4. READ ALL: Fetches all users for the Admin Management table
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT username, role FROM users"; // Password omitted for security display
        
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Password set to null or masked for display list
                list.add(new User(rs.getString("username"), "****", rs.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user list: " + e.getMessage());
        }
        return list;
    }
}