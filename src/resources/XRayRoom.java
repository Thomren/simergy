package resources;

import core.EmergencyDepartment;

/**
 * This is a class extending Rooms. It represents the XRay rooms of the Emergency Department.
 * It is needed to pass an XRay test.
 * @author Quentin
 *
 */

public class XRayRoom extends Room {

	public XRayRoom(String name, int capacity, EmergencyDepartment emergencyDepartment) {
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
		if (!(obj instanceof XRayRoom)) {
			return false;
		}
		return true;
	}
}
