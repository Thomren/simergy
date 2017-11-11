package resource;

import core.EmergencyDepartment;

public class Transporter extends Human {

	public Transporter(String name, String surname, EmergencyDepartment emergencyDepartment) {
		super(name, surname,  "idle", emergencyDepartment);
	}
}
