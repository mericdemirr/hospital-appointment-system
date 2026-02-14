import java.awt.EventQueue;

import dao.DatabaseHelper;
import guı.LoginFrame;

public class HospitalSystem {

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread (Best Practice for Swing)
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // 1. Initialize Database
                    // Check if hospital.db exists, if not create tables (Users, Patients, Doctors...)
                    System.out.println("Checking database...");
                    DatabaseHelper.createNewTable(); 
                    
                    // 2. Open the Login Screen
                    // We don't open MainFrame directly anymore. Security first!
                    System.out.println("Starting Login Screen...");
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

 