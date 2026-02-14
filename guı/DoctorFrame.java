package guı;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;
import dao.DoctorDAO;
import model.Doctor;

public class DoctorFrame extends JFrame {
    private JTextField txtId, txtName, txtSpecialty;
    private JTable table;
    private DefaultTableModel model;
    private DoctorDAO dao;

    public DoctorFrame() {
        dao = new DoctorDAO();
        setTitle("Doctor Management");
        setBounds(100, 100, 700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
  
        // Inputs
        JLabel lblId = new JLabel("Doctor ID:"); lblId.setBounds(20, 30, 80, 20);
        getContentPane().add(lblId);
        txtId = new JTextField(); txtId.setBounds(100, 30, 150, 20);
        getContentPane().add(txtId);

        JLabel lblName = new JLabel("Name:"); lblName.setBounds(20, 60, 80, 20);
        getContentPane().add(lblName);
        txtName = new JTextField(); txtName.setBounds(100, 60, 150, 20);
        getContentPane().add(txtName);

        JLabel lblSpec = new JLabel("Specialty:"); lblSpec.setBounds(20, 90, 80, 20);
        getContentPane().add(lblSpec);
        txtSpecialty = new JTextField(); txtSpecialty.setBounds(100, 90, 150, 20);
        getContentPane().add(txtSpecialty);

        // Buttons
        JButton btnAdd = new JButton("Add Doctor");
        btnAdd.setBounds(20, 130, 110, 30);
        getContentPane().add(btnAdd);

        // Table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(280, 20, 380, 300);
        getContentPane().add(scrollPane);
        table = new JTable();
        model = new DefaultTableModel();
        model.addColumn("ID"); model.addColumn("Name"); model.addColumn("Specialty");
        table.setModel(model);
        scrollPane.setViewportView(table);

        loadTableData();

        // Action: Add
        btnAdd.addActionListener(e -> {
            Doctor d = new Doctor(txtId.getText(), txtName.getText(), txtSpecialty.getText());
            if(dao.addDoctor(d)) {
                JOptionPane.showMessageDialog(null, "Doctor Added!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(null, "Error!");
            }
        });
    }

    private void loadTableData() {
        model.setRowCount(0);
        List<Doctor> list = dao.getAllDoctors();
        for(Doctor d : list) {
            model.addRow(new Object[]{d.getDoctorId(), d.getName(), d.getMedicalSpecialty()});
        }
    }
}