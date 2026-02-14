package guı;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import dao.*;
import model.*;

public class AppointmentFrame extends JFrame {
    private JTextField txtId, txtDate, txtTime;
    private JTextArea txtDiagnosis, txtPrescription;
    private JComboBox<String> comboPatients, comboDoctors;
    private JTable table;
    private DefaultTableModel model;
    private AppointmentDAO appDao;
    private User currentUser;

    public AppointmentFrame(User user) {
        this.currentUser = user;
        this.appDao = new AppointmentDAO();
        
        setTitle("Hospital System - Appointment Management");
        setBounds(100, 100, 950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        // UI Components
        JLabel lblId = new JLabel("App. ID:"); lblId.setBounds(20, 30, 100, 20); getContentPane().add(lblId);
        txtId = new JTextField(); txtId.setBounds(120, 30, 130, 25); getContentPane().add(txtId);

        JLabel lblPat = new JLabel("Patient:"); lblPat.setBounds(20, 70, 100, 20); getContentPane().add(lblPat);
        comboPatients = new JComboBox<>(); comboPatients.setBounds(120, 70, 130, 25); getContentPane().add(comboPatients);

        JLabel lblDoc = new JLabel("Doctor:"); lblDoc.setBounds(20, 110, 100, 20); getContentPane().add(lblDoc);
        comboDoctors = new JComboBox<>(); comboDoctors.setBounds(120, 110, 130, 25); getContentPane().add(comboDoctors);

        JLabel lblDate = new JLabel("Date:"); lblDate.setBounds(20, 150, 120, 20); getContentPane().add(lblDate);
        txtDate = new JTextField(java.time.LocalDate.now().toString()); txtDate.setBounds(120, 150, 130, 25); getContentPane().add(txtDate);

        JLabel lblTime = new JLabel("Time:"); lblTime.setBounds(20, 190, 100, 20); getContentPane().add(lblTime);
        txtTime = new JTextField("10:00"); txtTime.setBounds(120, 190, 130, 25); getContentPane().add(txtTime);

        JButton btnAdd = new JButton("➕ Create Appointment");
        btnAdd.setBackground(new Color(46, 204, 113)); btnAdd.setForeground(Color.WHITE);
        btnAdd.setBounds(20, 240, 230, 35); getContentPane().add(btnAdd);

        JButton btnDelete = new JButton("🗑️ Cancel Selected");
        btnDelete.setBackground(new Color(231, 76, 60)); btnDelete.setForeground(Color.WHITE);
        btnDelete.setBounds(20, 285, 230, 35); getContentPane().add(btnDelete);

        // Medical Notes Area
        JLabel lblDiag = new JLabel("Diagnosis:"); lblDiag.setBounds(20, 350, 230, 20); getContentPane().add(lblDiag);
        txtDiagnosis = new JTextArea(); txtDiagnosis.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDiagnosis.setBounds(20, 375, 230, 60); getContentPane().add(txtDiagnosis);

        JLabel lblPresc = new JLabel("Prescription:"); lblPresc.setBounds(20, 445, 230, 20); getContentPane().add(lblPresc);
        txtPrescription = new JTextArea(); txtPrescription.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtPrescription.setBounds(20, 470, 230, 60); getContentPane().add(txtPrescription);

        JButton btnSaveNotes = new JButton("💾 Save Medical Notes");
        btnSaveNotes.setBackground(new Color(52, 152, 219)); btnSaveNotes.setForeground(Color.WHITE);
        btnSaveNotes.setBounds(20, 545, 230, 40); getContentPane().add(btnSaveNotes);

        // Table
        JScrollPane scrollPane = new JScrollPane(); scrollPane.setBounds(280, 20, 630, 565); getContentPane().add(scrollPane);
        table = new JTable();
        model = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date", "Time", "Diagnosis", "Prescription"}, 0);
        table.setModel(model);
        scrollPane.setViewportView(table);

        // Access Control
        if ("doctor".equals(currentUser.getRole())) {
            btnAdd.setVisible(false); btnDelete.setVisible(false);
            txtId.setEditable(false); txtDate.setEditable(false); txtTime.setEditable(false);
        } else {
            txtDiagnosis.setEditable(false); txtPrescription.setEditable(false); btnSaveNotes.setVisible(false);
        }

        loadComboBoxes();
        refreshTableData();

        // Listeners
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtDiagnosis.setText(model.getValueAt(row, 5).toString());
                    txtPrescription.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            if(id.isEmpty()) { JOptionPane.showMessageDialog(null, "Please enter ID"); return; }
            String pId = comboPatients.getSelectedItem().toString().split(" - ")[0];
            String dId = comboDoctors.getSelectedItem().toString().split(" - ")[0];
            String date = txtDate.getText();
            String time = txtTime.getText();

            if (appDao.isDoctorAvailable(dId, date, time)) {
                if(appDao.addAppointment(id, pId, dId, date, time)) {
                    JOptionPane.showMessageDialog(null, "Success!");
                    refreshTableData();
                    txtId.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Database Error: Check if ID is unique.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Doctor Busy!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSaveNotes.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                if(appDao.updateMedicalNote(model.getValueAt(row, 0).toString(), txtDiagnosis.getText(), txtPrescription.getText())) {
                    JOptionPane.showMessageDialog(null, "Notes Saved!");
                    refreshTableData();
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                appDao.deleteAppointment(model.getValueAt(row, 0).toString());
                refreshTableData();
            }
        });
          
        setLocationRelativeTo(null);
    }

    private void loadComboBoxes() {
        new PatientDAO().getAllPatients().forEach(p -> comboPatients.addItem(p.getPatientId() + " - " + p.getName()));
        new DoctorDAO().getAllDoctors().forEach(d -> comboDoctors.addItem(d.getDoctorId() + " - " + d.getName()));
    }

    private void refreshTableData() {
        model.setRowCount(0);
        List<String[]> list;
        if ("doctor".equals(currentUser.getRole())) {
            // For demo, using Alice Smith. In real apps, use currentUser.getUsername()
            list = appDao.getAppointmentsByDoctor("Dr. Alice Smith");
        } else {
            list = appDao.getAllAppointmentsWithNames();
        }
        for (String[] row : list) model.addRow(row);
    }
}