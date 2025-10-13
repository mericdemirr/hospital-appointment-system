# Hospital-appointment-system
A simple, console-based hospital appointment system written in Java. It manages patients, doctors, and appointments, saving all data to .txt files for persistence.
## Features
Patient Management: Add and delete patients.
Doctor Management: Add and delete doctors.
Appointment Management: Create, list, update, and cancel appointments.
Data Persistence: Information is saved to and loaded from patients.txt, doctors.txt, and appointments.txt.
### How to Run
1.  **Compile:**
    ```bash
    javac *.java
    ```
2.  **Run:**
    ```bash
    java HospitalSystem
    ```
### File Structure
HospitalSystem.java: Main application class that runs the menu and handles file operations.
* Patient.java: Data model for patients.
* Doctor.java: Data model for doctors.
* Appointment.java: Data model for appointments.
