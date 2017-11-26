package resources;

import core.EmergencyDepartment;

public class Transporter extends Human {

	public Transporter(String name, String surname, EmergencyDepartment emergencyDepartment) {
		super(name, surname,  "idle", emergencyDepartment);
	}
	
	@Override
	public String toString() {
		return "Transporter " + name + " " + surname;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Transporter)) {
			return false;
		}
		return true;
	}
}
