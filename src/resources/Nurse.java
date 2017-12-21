package resources;

import core.EmergencyDepartment;

/**
 * The Nurse class represents a nurse of the Emergency Department
 * @author Thomas
 *
 */
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
