package resources;

import core.EmergencyDepartment;
import core.Entity;

/**
 * Stretcher is a class to represent the stretchers of the Emergency Department.
 * It inherits from Entity.
 * @author Quentin
 *
 */

public class Stretcher extends Entity {

	public Stretcher(EmergencyDepartment emergencyDepartment) {
		super("stretcher", emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

	public Stretcher(String name, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}
	
}
