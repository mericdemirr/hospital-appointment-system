package guı;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.PatientDAO;
import model.Patient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PatientFrame extends JFrame {

    private JPanel contentPane;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtPhone;
    private JTable table;
    private DefaultTableModel model; 
    private PatientDAO dao; 

    public PatientFrame() {
        dao = new PatientDAO(); 

        setTitle("Patient Management System");
        // DISPOSE_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setBounds(100, 100, 750, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- THE INPUT FIELDS ON THE LEFT SIDE ---
        
        JLabel lblId = new JLabel("Patient ID:");
        lblId.setBounds(20, 30, 80, 20);
        contentPane.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 30, 150, 20);
        contentPane.add(txtId);

        JLabel lblName = new JLabel("Full Name:");
        lblName.setBounds(20, 60, 80, 20);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(100, 60, 150, 20);
        contentPane.add(txtName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(20, 90, 80, 20);
        contentPane.add(lblAge);

        txtAge = new JTextField();
        txtAge.setBounds(100, 90, 150, 20);
        contentPane.add(txtAge);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(20, 120, 80, 20);
        contentPane.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(100, 120, 150, 20);
        contentPane.add(txtPhone);

        // --- BUTTONS ---  

        JButton btnAdd = new JButton("Add Patient");
        btnAdd.setBounds(20, 160, 110, 30);
        contentPane.add(btnAdd);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBounds(140, 160, 130, 30);
        contentPane.add(btnDelete);

        // --- THE TABLE (LIST) ON THE RIGHT SIDE ---
        
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(280, 20, 430, 350);
        contentPane.add(scrollPane);

        table = new JTable();
        model = new DefaultTableModel();
        
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Phone");
        
        table.setModel(model);
        scrollPane.setViewportView(table);

       
        loadTableData();

        // --- BUTTON ACTİONS ---

        // 1. Add BUTTON
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String id = txtId.getText();
                String name = txtName.getText();
                String phone = txtPhone.getText();
                
                
                if(id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ID and Name cannot be empty!");
                    return;
                }
                
                int age = 0;
                try {
                    age = Integer.parseInt(txtAge.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for Age!");
                    return;
                }

                
                Patient newPatient = new Patient(id, name, age, phone);
                
                if (dao.addPatient(newPatient)) {
                    JOptionPane.showMessageDialog(null, "Patient Added Successfully!");
                    loadTableData(); 
                   
                    txtId.setText("");
                    txtName.setText("");
                    txtAge.setText("");
                    txtPhone.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Error! This ID might already exist.");
                }
            }
        });

        // 2. Delete BUTTON
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a patient from the table to delete.");
                } else {
                    
                    String idToDelete = (String) model.getValueAt(selectedRow, 0);
                    
                    int confirm = JOptionPane.showConfirmDialog(null, "Delete patient ID: " + idToDelete + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if(confirm == JOptionPane.YES_OPTION) {
                        dao.deletePatient(idToDelete); 
                        loadTableData(); 
                        JOptionPane.showMessageDialog(null, "Patient Deleted.");
                    }
                }
            }
        });
    }

    // Helper method
    private void loadTableData() {
     
        model.setRowCount(0);
          
        List<Patient> list = dao.getAllPatients();
             
        for (Patient p : list) {
            model.addRow(new Object[]{ p.getPatientId(), p.getName(), p.getAge(), p.getPhoneNumber() });
        }
    }
}