package guı;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.UserDAO;
import model.User;

/**
 * AdminFrame manages system-wide user accounts.
 * It allows the administrator to grant or revoke access for Doctors and Secretaries.
 */
public class AdminFrame extends JFrame {
    private JTextField txtUser, txtPass;
    private JComboBox<String> comboRole;
    private JTable table;
    private DefaultTableModel model;
    private UserDAO userDAO;
  
    public AdminFrame() {
        userDAO = new UserDAO();
        
        // --- FRAME CONFIGURATION ---
        setTitle("System Administration - User Management");
        setBounds(150, 150, 600, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        // --- INPUT FIELDS SECTION ---
        JLabel lblUser = new JLabel("Username:"); 
        lblUser.setBounds(20, 20, 80, 20);
        getContentPane().add(lblUser);
        
        txtUser = new JTextField(); 
        txtUser.setBounds(100, 20, 150, 25);
        getContentPane().add(txtUser);

        JLabel lblPass = new JLabel("Password:"); 
        lblPass.setBounds(20, 60, 80, 20);
        getContentPane().add(lblPass);
        
        txtPass = new JTextField(); 
        txtPass.setBounds(100, 60, 150, 25);
        getContentPane().add(txtPass);

        JLabel lblRole = new JLabel("System Role:"); 
        lblRole.setBounds(20, 100, 80, 20);
        getContentPane().add(lblRole);
        
        comboRole = new JComboBox<>(new String[]{"secretary", "doctor", "admin"});
        comboRole.setBounds(100, 100, 150, 25);
        getContentPane().add(comboRole);

        // --- ACTION BUTTONS ---
        
        // Button to register new staff
        JButton btnAdd = new JButton("➕ Register New User");
        btnAdd.setBackground(new Color(46, 204, 113)); // Emerald Green
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBounds(20, 150, 230, 35);
        getContentPane().add(btnAdd);

        // Button to revoke user access
        JButton btnDelete = new JButton("🗑️ Terminate Access");
        btnDelete.setBackground(new Color(230, 126, 34)); // Carrot Orange
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBounds(20, 195, 230, 35);
        getContentPane().add(btnDelete);

        // --- USER LIST TABLE ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(280, 20, 280, 380);
        getContentPane().add(scrollPane);
        
        table = new JTable();
        model = new DefaultTableModel(new Object[]{"Username", "Role"}, 0);
        table.setModel(model);
        scrollPane.setViewportView(table);

        // Initialize table data
        loadUsers();

        // --- EVENT LISTENERS ---

        // Logic to add a new unique user to the SQLite database
        btnAdd.addActionListener(e -> {
            String u = txtUser.getText();
            String p = txtPass.getText();
            String r = comboRole.getSelectedItem().toString();

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields!");
                return;
            }

            if (userDAO.addUser(u, p, r)) {
                JOptionPane.showMessageDialog(null, "Success: User registered!");
                loadUsers(); // Refresh the list
                txtUser.setText("");
                txtPass.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Username exists or database error!");
            }
        });

        // Logic to remove a user's access
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String uToDelete = model.getValueAt(row, 0).toString();
                
                // Final confirmation before deletion
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to revoke access for: " + uToDelete + "?", 
                    "Confirm Action", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (userDAO.deleteUser(uToDelete)) {
                        JOptionPane.showMessageDialog(null, "Access revoked successfully.");
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(null, "Warning: Primary administrator cannot be removed!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a user from the table.");
            }
        });
        
        // Center window on startup
        setLocationRelativeTo(null);
    }

    /**
     * Fetches all registered users from the DAO and updates the table view.
     */
    private void loadUsers() {
        model.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            model.addRow(new Object[]{u.getUsername(), u.getRole()});
        }
    }
}