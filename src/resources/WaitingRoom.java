package resources;

import core.EmergencyDepartment;

/**
 * WaitingRoom is a class which represent the Waiting Rooms of the Emergency Department.
 * It extends Rooms.
 * @author Quentin
 *
 */

public class WaitingRoom extends Room {

	public WaitingRoom(String name, int capacity, EmergencyDepartment emergencyDepartment) {
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
		if (!(obj instanceof WaitingRoom)) {
			return false;
		}
		return true;
	}
	
}
