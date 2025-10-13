import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HospitalSystem {
	private static final String PATIENTS_FILE = "patients.txt";
	private static final String DOCTORS_FILE = "doctors.txt";			
	private static final String APPOINTMENTS_FILE = "appointments.txt";

	static Map<String, Patient>patients = new HashMap<>();
	static Map<String, Doctor>doctors = new HashMap<>();
	static Map<String, Appointment>appointments = new HashMap<>();

	public static void main(String[] args) {
		createFilesIfNotExist();
		loadPatientsFromFile();
		loadDoctorsFromFile();
		loadAppointmentsFromFile();

		
		Scanner scanner = new Scanner(System.in);
		int choice;  

		do{		
			System.out.println("\n___ MMD_Hospital_Appointment_System ___");
			System.out.println("1. Add a patient");
			System.out.println("2. Add a doctor");
			System.out.println("3. Create an appointment");
			System.out.println("4. List appointments");
			System.out.println("5. Update an appointment");
			System.out.println("6. Remove an appointment");
			System.out.println("7. Delete a doctor");
			System.out.println("8. Delete a patient");
			System.out.println("9. Exit");
			System.out.print("Choice:");

			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1: addPatient();break;
			case 2: addDoctor();break;
			case 3: createAppointment();break;
			case 4: listAppointments();break;
			case 5: updateAppointment();break;
			case 6: removeAppointment();break;
			case 7: removeDoctor();break;
			case 8: removePatient();break;
			case 9: System.out.println("Exit ...");
			default: System.out.println("Invalid choice.");
			}

		} while (choice != 9);
		scanner.close();
	}
	public static void addPatient() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a patientId: ");
		String patientId = scanner.nextLine();

		if(patients.containsKey(patientId)) {
			System.out.println("A patient with the same ID already exists");
			return;
		}
		System.out.print("Enter a patient name: ");
		String name = scanner.nextLine();

		System.out.print("Enter a patient age: ");
		int age = scanner.nextInt();

		System.out.print("Enter a patient phone number: ");
		String phoneNumber = scanner.nextLine();

		Patient newPatient = new Patient(patientId ,name ,age ,phoneNumber);

		patients.put(patientId, newPatient);

		System.out.println("Patient was added successfully");
	 
		savePatientsToFile();
	}

	public static void addDoctor() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a doctorId: ");
		String doctorId = scanner.nextLine();

		if(doctors.containsKey(doctorId)) {
			System.out.println("A doctor with the same ID already exists");
			return;
		}
		System.out.print("Enter a doctor name: ");
		String name = scanner.nextLine();

		System.out.print("Enter a doctor specialty: ");
		String medicalSpecialty = scanner.nextLine();

		Doctor newDoctor = new Doctor(doctorId, name ,medicalSpecialty );

		doctors.put(doctorId, newDoctor);

		System.out.println("Doctor was added successfully");
		
		saveDoctorsToFile();

	}

	public static void createAppointment() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a patientId: ");
		String patientId = scanner.nextLine();

		Patient patient = patients.get(patientId);

		if(patient == null) {
			System.out.println("No such patient found.");
			return;
		}
		System.out.print("Enter a doctorId: ");
		String doctorId = scanner.nextLine();

		Doctor doctor = doctors.get(doctorId);

		if(doctor == null) {
			System.out.println("No such doctor found.");
			return;
		}

		System.out.print("Appointment Date (YYYY-MM-DD): ");
		String date = scanner.nextLine();

		System.out.print("Appointment Time (HH:MM): ");
		String time = scanner.nextLine();

		boolean isAvailable = true;

		for(Appointment appt : appointments.values()) {
			if(appt.getDoctor().getDoctorId().equals(doctorId)&&
					appt.getdate().equals(date) &&
					appt.gettime().equals(time)) {
				isAvailable = false;
				break;
			}

		}			
		if(!isAvailable) {
			System.out.println("This doctor already has another appointment at that date and time.");
			return;			
		}
		String appointmentId = "A" + (appointments.size() +1 );
		Appointment newAppointment = new Appointment(appointmentId, patient ,doctor , date, time);
		appointments.put(appointmentId, newAppointment);
		System.out.println("Appointment was created succesfully");

		saveAppointmentsToFile();
	}

	public static void listAppointments() {
	    if (appointments.isEmpty()) {
	        System.out.println("There is no appointment currently registered.");
	        return;
	    }
	    for (Appointment appt : appointments.values()) {
	        System.out.println(appt.toString()); 
	        System.out.println("____________________________");
	    }
	}

	public static void updateAppointment() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a appointmentId that you update: ");
		String appointmentId = scanner.nextLine();
		Appointment appointment = appointments.get(appointmentId);

		if(appointment == null) {
			System.out.println("No such appointment found.");
			return;
		}

		System.out.println("What do you want to choose");
		System.out.println("1. Date");
		System.out.println("2. Time");
		System.out.println("3. Doctor");
		System.out.println("4. Cancel");
		System.out.print("Choice:");

		int choice = scanner.nextInt();
		scanner.nextLine();

		switch(choice) {
		case 1: 
			System.out.println("Enter a new date (YYYY-MM-DD): ");
			String newDate = scanner.nextLine();
			if(isAvailable(appointment.getDoctor().getDoctorId(),newDate,appointment.gettime(),appointmentId)) {
				appointment.setdate(newDate);
				System.out.println("Date was updated.");
			}else {
				System.out.println("There is another appointment at this date and time.");
			}
			break;
		case 2: 
			System.out.println("Enter a new time(HH:MM): ");
			String newTime = scanner.nextLine();
			if(isAvailable(appointment.getDoctor().getDoctorId(),appointment.getdate(),newTime,appointmentId)) {
				appointment.settime(newTime);
				System.out.println("Time was updated.");
			}else {
				System.out.println("There is another appointment at this time.");
			}
			break;
		case 3: 
			System.out.println("Enter a new doctorId: ");
			String newDoctorId = scanner.nextLine();
			Doctor newDoctor = doctors.get(newDoctorId);

			if(newDoctor == null) {
				System.out.println("No such doctor found.");
			}else if(isAvailable(newDoctorId, appointment.getdate(), appointment.gettime(), appointmentId)) {
				appointment.setDoctor(newDoctor);
				System.out.println("Doctor was updated.");
			}else {
				System.out.println("This doctor already has another appointment at this date and time.");
			}
			break;
		case 4:
			System.out.println("The operation has been cancelled.");
			break;

		default:
			System.out.println("İnvalid choice");
		}

	}

	public static boolean isAvailable(String doctorId, String date, String time , String appointmentIdSkip){

		for(Appointment appt : appointments.values()) {
			if(!appt.getAppointmentId().equals(appointmentIdSkip)&&
					appt.getDoctor().getDoctorId().equals(doctorId)&&
					appt.getdate().equals(date) &&
					appt.gettime().equals(time)) {
				return false;
			}

		}	
		return true;
	}

	public static void removeAppointment() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a appointmentId that you remove: ");
		String appointmenttId = scanner.nextLine();

		if(appointments.containsKey(appointmenttId)) {
			appointments.remove(appointmenttId);
			System.out.println("Appointment was removed succcessfully.");
		}else {
			System.out.println("No such appointment found.");
		}

	}
	//For example, if a doctor quits their job, you should remove them from the appointment system. This method, removeDoctor(), ensures that.

	public static void removeDoctor() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a doctorId that you remove: ");
		String doctorId = scanner.nextLine();

		if(!doctors.containsKey(doctorId)) {
			System.out.println("No such doctor found.");
			return;
		}
		appointments.values().removeIf(appt -> appt.getDoctor().getDoctorId().equals(doctorId));
		doctors.remove(doctorId);
		System.out.println("Doctor was removed succcessfully.");

	}
	
	public static void removePatient() {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter a patientId that you remove: ");
		String patientId = scanner.nextLine();

		if(!patients.containsKey(patientId)) {
			System.out.println("No such patient found.");
			return;
		}
		appointments.values().removeIf(appt -> appt.getPatient().getPatientId().equals(patientId));
		patients.remove(patientId);
		System.out.println("Patient was removed succcessfully.");

		savePatientsToFile();
	}
	public static void createFilesIfNotExist() {
	    try {
	    	if (!new File(PATIENTS_FILE).exists()) {
                new File(PATIENTS_FILE).createNewFile();
	            System.out.println("patients.txt file created.");
	        }
	    	if (!new File(DOCTORS_FILE).exists()) {
                new File(DOCTORS_FILE).createNewFile();
	            System.out.println("doctors.txt file created.");
	        }
	    	if (!new File(APPOINTMENTS_FILE).exists()) {
                new File(APPOINTMENTS_FILE).createNewFile();
	            System.out.println("appointments.txt file created.");
	        }
	    } catch (IOException e) {
	        System.out.println("Error creating files: " + e.getMessage());
	    }
	}
	public static void savePatientsToFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(PATIENTS_FILE));

			for (Patient p : patients.values()) {
				String line = p.toString();
				writer.write(line);
				writer.newLine();
			}

			writer.close();
			System.out.println("Recorded patients");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void loadPatientsFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_FILE));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String patientId = parts[0];
				String name = parts[1];
				int age = Integer.parseInt(parts[2]);
				String phoneNumber = parts[3];

				Patient patient = new Patient(patientId, name,age, phoneNumber);
				patients.put(patientId, patient);
			}

			reader.close();
			System.out.println("Loaded patients");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void saveDoctorsToFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(DOCTORS_FILE));

			for (Doctor d : doctors.values()) {
				String line = d.toString();
				writer.write(line);
				writer.newLine();
			}

			writer.close();
			System.out.println("Recorded doctors");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void loadDoctorsFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(DOCTORS_FILE));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String doctorId = parts[0];
				String name = parts[1];
				String medicalSpecialty = parts[2];


				Doctor doctor = new Doctor(doctorId, name, medicalSpecialty);
				doctors.put(doctorId, doctor);
			}

			reader.close();
			System.out.println("Loaded doctors");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void saveAppointmentsToFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE));

			for (Appointment a : appointments.values()) {
				String line = a.toString();
				writer.write(line);
				writer.newLine();
			}

			writer.close();
			System.out.println("Recorded appointments");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	public static void loadAppointmentsFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_FILE));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				String appointmentId = parts[0];
				String patientId = parts[1];
				String doctorId = parts[2];
				String date = parts[3];
				String time = parts[4];

				Patient patient = patients.get(patientId);
				Doctor doctor = doctors.get(doctorId);

				if(patient != null && doctor != null) {
					Appointment appointment = new Appointment(appointmentId, patient, doctor, date, time);
					appointments.put(appointmentId, appointment);
				}
			}
			reader.close();
			System.out.println("Loaded appointments");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}


