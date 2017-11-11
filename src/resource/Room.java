package resource;

import java.util.ArrayList;

import core.EmergencyDepartment;
import core.Entity;

/**
 * Room is an abstract class to represent every room in the Emergency Department.
 * It has a capacity.
 * It inherits from Entity.
 * @author Quentin
 * @author Thomas
 *
 */

public abstract class Room extends Entity {
	protected int capacity;
	protected ArrayList<Patient> patients;
	
	public Room(String name, int capacity, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.capacity = capacity;
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method return true if there is fewer patients than the capacity of the room, false if not
	 * @return
	 */
	public boolean isAvailable() {
		return capacity > patients.size();
	}

}
