package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;

public class DoctorDAO {

    // Method to add a doctor
    public boolean addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors(id, name, specialty) VALUES(?,?,?)";
  
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, doctor.getDoctorId());
            pstmt.setString(2, doctor.getName());
            pstmt.setString(3, doctor.getMedicalSpecialty());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding doctor: " + e.getMessage());
            return false;
        }
    }

    // Method to list all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("specialty")
                );
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error listing doctors: " + e.getMessage());
        }
        return list;
    }

    // Method to delete a doctor
    public void deleteDoctor(String id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }
}