package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class AppointmentDAO {

    // Adds a new appointment with empty diagnosis and prescription
    public boolean addAppointment(String id, String patientId, String doctorId, String date, String time) {
        String sql = "INSERT INTO appointments(id, patient_id, doctor_id, date, time, diagnosis, prescription) VALUES(?,?,?,?,?,'','')";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
              
            pstmt.setString(1, id);
            pstmt.setString(2, patientId);
            pstmt.setString(3, doctorId);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Insert Error: " + e.getMessage());
            return false;
        }
    }

    // Fetches all appointments using LEFT JOIN to ensure visibility
    public List<String[]> getAllAppointmentsWithNames() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, IFNULL(p.name, 'Unknown') as p_name, IFNULL(d.name, 'Unknown') as d_name, " +
                     "a.date, a.time, a.diagnosis, a.prescription " +
                     "FROM appointments a " +
                     "LEFT JOIN patients p ON a.patient_id = p.id " +
                     "LEFT JOIN doctors d ON a.doctor_id = d.id";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"), rs.getString("p_name"), rs.getString("d_name"),
                    rs.getString("date"), rs.getString("time"), rs.getString("diagnosis"), rs.getString("prescription")
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    // Filters appointments for a specific doctor
    public List<String[]> getAppointmentsByDoctor(String doctorName) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.id, p.name as p_name, d.name as d_name, a.date, a.time, a.diagnosis, a.prescription " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.id " +
                     "JOIN doctors d ON a.doctor_id = d.id " +
                     "WHERE d.name = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, doctorName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"), rs.getString("p_name"), rs.getString("d_name"),
                    rs.getString("date"), rs.getString("time"), rs.getString("diagnosis"), rs.getString("prescription")
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    public boolean updateMedicalNote(String appointmentId, String diagnosis, String prescription) {
        String sql = "UPDATE appointments SET diagnosis = ?, prescription = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diagnosis);
            pstmt.setString(2, prescription);
            pstmt.setString(3, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public void deleteAppointment(String id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    public boolean isDoctorAvailable(String doctorId, String date, String time) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND date = ? AND time = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctorId);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) == 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return false;
    }
}