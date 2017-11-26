package resources;

import core.EmergencyDepartment;

/**
 * This is a class extending Rooms. It represents the Blood test rooms of the Emergency Department.
 * It is needed to pass a blood test.
 * @author Quentin
 *
 */

public class BloodTestRoom extends Room {

	public BloodTestRoom(String name, int capacity, EmergencyDepartment emergencyDepartment) {
		super(name, capacity, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof BloodTestRoom)) {
			return false;
		}
		return true;
	}

}
