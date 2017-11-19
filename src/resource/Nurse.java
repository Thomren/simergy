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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Nurse)) {
			return false;
		}
		return true;
	}
}
