package resource;

import core.EmergencyDepartment;

public class Nurse extends Human {

	public Nurse(String name, String surname, EmergencyDepartment emergencyDepartment) {
		super(name, surname,  "idle", emergencyDepartment);
	}
	
	@Override
	public String toString() {
		return "Nurse " + name + " " + surname;
	}
}
