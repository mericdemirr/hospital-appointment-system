public class Appointment {
	private String appointmentId;
	private Patient patient;
	private Doctor doctor;
	private String date;
	private String time;
	
	public	Appointment(String appointmentId ,Patient patient ,Doctor doctor ,String date ,String time) {
		this.appointmentId = appointmentId;
		this.patient = patient;
		this.doctor = doctor;
		this.date = date;
		this.time = time;
	}

	public String getAppointmentId(){
		return appointmentId;
	}

	public Patient getPatient(){
		return patient;
	}

	public Doctor getDoctor(){
		return doctor;
	}

	public String getdate(){
		return date;
	}

	public String gettime(){
		return time;
	}

	public void setDoctor(Doctor Doctor){
		this.doctor = doctor;
	}

	public void setdate(String date){
		this.date = date;
	}

	public void settime(String time){
		this.time = time;
	}

	@Override
	public String toString() {
        return "Appointment ID: " + appointmentId + ", " + patient.toString() + ", " + doctor.toString() + ", Date: " + date + ", Time: " + time;
    }
}
