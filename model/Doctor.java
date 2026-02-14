package model;
public class Doctor {
	private String doctorId;
	private String name;
	private String medicalSpecialty;
	

	public Doctor(String doctorId ,String name ,String medicalSpecialty){
		this.doctorId = doctorId;
		this.name = name;
		this.medicalSpecialty = medicalSpecialty;
	}

	public String getDoctorId(){
		return doctorId;
	}
  
	public String getName(){
		return name;
	}

	public String getMedicalSpecialty(){
		return medicalSpecialty;
	}

	@Override
	public String toString() {
        return "Doctor ID: " + doctorId + ", Name: " + name + ", Specialty: " + medicalSpecialty;
    }
}