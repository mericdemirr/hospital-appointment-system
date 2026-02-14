package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseHelper manages the SQLite database connection and 
 * handles the initial setup of tables and seed data.
 */
public class DatabaseHelper {
    
    private static final String DB_URL = "jdbc:sqlite:hospital.db";

    // Establish a connection to the SQLite database file
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return conn;
    }
  
    // Initialize the database schema by creating all necessary tables
    public static void createNewTable() {
        // Table for storing patient information
        String sqlPatients = "CREATE TABLE IF NOT EXISTS patients ("
                + " id text PRIMARY KEY,"
                + " name text NOT NULL,"
                + " age integer,"
                + " phone text"
                + ");";

        // Table for storing doctor information
        String sqlDoctors = "CREATE TABLE IF NOT EXISTS doctors ("
                + " id text PRIMARY KEY,"
                + " name text NOT NULL,"
                + " specialty text"
                + ");";

     // Appointments Table with Diagnosis and Prescription
        String sqlAppointments = "CREATE TABLE IF NOT EXISTS appointments ("
                + " id text PRIMARY KEY,"
                + " patient_id text,"
                + " doctor_id text,"
                + " date text,"
                + " time text,"
                + " diagnosis text,"    
                + " prescription text," 
                + " FOREIGN KEY (patient_id) REFERENCES patients (id),"
                + " FOREIGN KEY (doctor_id) REFERENCES doctors (id)"
                + ");";
        
        // Table for system authentication and authorization
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users ("
                + " username text PRIMARY KEY,"
                + " password text NOT NULL,"
                + " role text NOT NULL" 
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            
            // Execute schema creation
            stmt.execute(sqlPatients);
            stmt.execute(sqlDoctors);
            stmt.execute(sqlAppointments);
            stmt.execute(sqlUsers);
            
            System.out.println("Database schema initialized.");
            
            // Populate initial system users and sample data
            createDefaultAdmin(conn);
            initializeSystemData(conn);

        } catch (SQLException e) {
            System.out.println("Schema creation error: " + e.getMessage());
        }
    }
    
    // Populates the database with initial accounts and medical records for testing
    public static void initializeSystemData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            
            // 1. Setup system users for different roles
            stmt.execute("INSERT OR IGNORE INTO users(username, password, role) VALUES('secretary', '1234', 'secretary')");
            stmt.execute("INSERT OR IGNORE INTO users(username, password, role) VALUES('doctor', '1234', 'doctor')");
            
            // 2. Setup sample medical records
            stmt.execute("INSERT OR IGNORE INTO doctors(id, name, specialty) VALUES('D101', 'Dr. Alice Smith', 'Cardiology')");
            stmt.execute("INSERT OR IGNORE INTO patients(id, name, age, phone) VALUES('P500', 'John Doe', 34, '555-0102')");
            
            // 3. Setup a sample appointment record (Today: 2026-02-14)
            stmt.execute("INSERT OR IGNORE INTO appointments(id, patient_id, doctor_id, date, time) " +
                         "VALUES('A1', 'P500', 'D101', '2026-02-14', '14:30')");

            System.out.println("Sample system data successfully initialized.");

        } catch (SQLException e) {
            System.out.println("Data initialization error: " + e.getMessage());
        }
    }
    
    // Ensures a root administrator account is always available
    private static void createDefaultAdmin(Connection conn) {
        String checkSql = "SELECT count(*) FROM users WHERE username = 'admin'";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO users(username, password, role) VALUES('admin', '1234', 'admin')";
                stmt.executeUpdate(insertSql);
                System.out.println("Default admin user created.");
            }
        } catch (SQLException e) {
            System.out.println("Error verifying admin user: " + e.getMessage());
        }
    }
}