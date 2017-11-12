package resource;

import core.EmergencyDepartment;

public class Transporter extends Human {

	public Transporter(String name, String surname, EmergencyDepartment emergencyDepartment) {
		super(name, surname,  "idle", emergencyDepartment);
	}
	
	@Override
	public String toString() {
		return "Transporter " + name + " " + surname;
	}
}
