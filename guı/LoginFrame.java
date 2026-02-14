package guı;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.DatabaseHelper;
import dao.UserDAO;
import model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame handles user authentication and session initiation.
 * It serves as the secure gateway to the Hospital Management System.
 */
public class LoginFrame extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Ensure database schema is ready on startup
                    DatabaseHelper.createNewTable(); 
                    
                    LoginFrame frame = new LoginFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        });
    }

    public LoginFrame() {
        // UI Layout and Styling
        setTitle("Hospital Management - Secure Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 320);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Welcome to MMD Hospital");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 20, 414, 30);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUsername.setBounds(50, 80, 80, 20);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(140, 80, 200, 25);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPassword.setBounds(50, 130, 80, 20);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(140, 130, 200, 25);
        contentPane.add(txtPassword);

        // --- LOGIN ACTION ---
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLogin.setBounds(140, 180, 100, 35);
        contentPane.add(btnLogin);
        
        JLabel lblStatus = new JLabel("");
        lblStatus.setForeground(Color.RED);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBounds(10, 230, 414, 20);
        contentPane.add(lblStatus);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                // Use UserDAO to verify credentials in SQLite
                UserDAO userDAO = new UserDAO();
                User user = userDAO.login(username, password);

                if (user != null) {
                    // Authentication Successful: Initiate Session
                    lblStatus.setForeground(new Color(46, 204, 113)); // Success Green
                    lblStatus.setText("Access Granted! Loading dashboard...");
                    
                    // Brief delay could be added here for UX, but dispose is fine
                    dispose(); 
                    
                    // Pass the authenticated 'User' object to the MainFrame
                    // This is crucial for Role-Based Access Control (RBAC)
                    MainFrame mainFrame = new MainFrame(user);
                    mainFrame.setVisible(true);
                    
                } else {
                    // Authentication Failed: Provide feedback
                    lblStatus.setForeground(Color.RED);
                    lblStatus.setText("Invalid username or password!");
                }
            }
        });
    }
}