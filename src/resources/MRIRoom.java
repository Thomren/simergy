package resources;

import core.EmergencyDepartment;

/**
 * This is a class extending Rooms. It represents the MRI rooms of the Emergency Department.
 * It is needed to pass a MRI test.
 * @author Quentin
 *
 */

public class MRIRoom extends Room {

	public MRIRoom(String name, int capacity, EmergencyDepartment emergencyDepartment) {
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
		if (!(obj instanceof MRIRoom)) {
			return false;
		}
		return true;
	}
	
}
