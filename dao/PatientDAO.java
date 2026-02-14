package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Patient;

public class PatientDAO {

    // 1. CREATE
    public boolean addPatient(Patient patient) {
        
        String sql = "INSERT INTO patients(id, name, age, phone) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
           
            pstmt.setString(1, patient.getPatientId());
            pstmt.setString(2, patient.getName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getPhoneNumber());
            
            pstmt.executeUpdate(); 
            return true; 
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
            return false; 
        }
    }

    // 2. READ
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("phone")
                );
                list.add(p); 
            }
        } catch (SQLException e) {
            System.out.println("Error listing patients: " + e.getMessage());
        }
        return list;
    }

    // 3. DELETE
    
    public boolean deletePatient(String id) {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }
      
    // 4. CHECK
    public Patient getPatientById(String id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Patient(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding patient: " + e.getMessage());
        }
        return null;
    }
}