package model;
public class Patient {
	private String patientId;
	private String name;
	private int age;
	private String phoneNumber;

	public Patient(String patientId ,String name ,int age ,String phoneNumber){
		this.patientId = patientId;
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}

	public String getPatientId(){
		return patientId;
	}

	public String getName(){
		return name;
	}

	public int getAge(){
		return age;
	}
  
	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setAge(int age){
		this.age = age;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
        return "Patient ID: " + patientId + ", Name: " + name + ", Age: " + age + ", Phone: " + phoneNumber;
    }
}
