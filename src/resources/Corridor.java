package resources;

import core.EmergencyDepartment;

/**
 * This class is a room with an infinite capacity representing the corridors of the hospital, where people
 * are put if there is no WaitingRoom available
 * @author Thomas
 *
 */
public class Corridor extends Room {

	public Corridor(EmergencyDepartment emergencyDepartment) {
		super("Corridor", -1, emergencyDepartment);
	}
	
	/**
	 * This method shadow the method of room, always returning true to simulate an infinite capacity
	 * @return True
	 */
	public boolean isAvailable() {
		return true;
	}
	

}
