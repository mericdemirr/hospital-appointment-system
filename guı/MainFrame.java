package guı;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * MainFrame serves as the central dashboard of the application.
 * It manages navigation and enforces Role-Based Access Control (RBAC).
 */
public class MainFrame extends JFrame {

    private JPanel contentPane;
    private User currentUser; 

    public MainFrame(User user) {
        this.currentUser = user; 

        // --- UI CONFIGURATION ---
        setTitle("Hospital Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Welcome Header showing Username and Role
        JLabel lblWelcome = new JLabel("Welcome, " + user.getUsername() + " [" + user.getRole().toUpperCase() + "]");
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblWelcome.setBounds(20, 20, 500, 30);
        contentPane.add(lblWelcome);
        //update for test 
        // --- NAVIGATION BUTTONS ---

        JButton btnPatients = new JButton("Patient Management");
        btnPatients.setBounds(50, 80, 220, 50);
        contentPane.add(btnPatients);

        JButton btnAppointments = new JButton("Appointment Schedule");
        btnAppointments.setBounds(310, 80, 220, 50);
        contentPane.add(btnAppointments);
        
        JButton btnDoctors = new JButton("Doctor Operations");
        btnDoctors.setBounds(50, 160, 220, 50);
        contentPane.add(btnDoctors);

        // System Admin Tools (Highlighted in Red for prominence)
        JButton btnDeleteDoctor = new JButton("System Admin Tools");
        btnDeleteDoctor.setForeground(Color.RED);
        btnDeleteDoctor.setBounds(310, 160, 220, 50);
        contentPane.add(btnDeleteDoctor);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(460, 360, 100, 30);
        contentPane.add(btnLogout);

        // --- ROLE-BASED ACCESS CONTROL (RBAC) LOGIC ---
        
        // [IMPORTANT] If the user is NOT an admin, restrict management features
        if (!"admin".equals(user.getRole())) {
            
            // Disable Admin Tools for both Secretaries and Doctors
            btnDeleteDoctor.setEnabled(false);
            btnDeleteDoctor.setText("Admin Access Only");

            // Disable Doctor Management for both Secretaries and Doctors
            // (Only Admin can hire/fire/edit doctor staff)
            btnDoctors.setEnabled(false);
            btnDoctors.setText("Admin Access Only");
        }

        // Additional restriction: Doctors usually don't need the general patient list
        if ("doctor".equals(user.getRole())) {
            btnPatients.setEnabled(false);
            btnPatients.setText("View via Appointments");
        }

        // --- BUTTON ACTION LISTENERS ---

        // 1. Open Admin Panel (Security: Only works if button is enabled)
        btnDeleteDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("admin".equals(currentUser.getRole())) {
                    AdminFrame adminPanel = new AdminFrame();
                    adminPanel.setVisible(true);
                }
            }
        });
        
        // 2. Open Appointment Schedule
        btnAppointments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Pass current user to AppointmentFrame for data filtering (Doctor privacy)
                AppointmentFrame appFrame = new AppointmentFrame(currentUser); 
                appFrame.setVisible(true);
            }
        });

        // 3. Open Patient Management
        btnPatients.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PatientFrame pFrame = new PatientFrame();
                pFrame.setVisible(true);
            }
        });

        // 4. Open Doctor Management (Only Admin will reach here)
        btnDoctors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DoctorFrame dFrame = new DoctorFrame();
                dFrame.setVisible(true);
            }
        });

        // 5. Logout and Return to Login Screen
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
                dispose(); // Close current session dashboard
            }
        });
        
        // Center the frame on the screen for better UX
        setLocationRelativeTo(null);
    }
}