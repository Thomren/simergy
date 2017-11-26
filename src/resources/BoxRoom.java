package resources;

import core.EmergencyDepartment;

/**
 * BoxRoom is a class which represent the Box Rooms of the Emergency Department.
 * It extends Rooms.
 * @author Quentin
 *
 */

public class BoxRoom extends Room {

	public BoxRoom(String name, int capacity, EmergencyDepartment emergencyDepartment) {
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
		if (!(obj instanceof BoxRoom)) {
			return false;
		}
		return true;
	}
	
}
