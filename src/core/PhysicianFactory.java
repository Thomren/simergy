package core;

import resource.Physician;

/**
 * Factory to generate nurses
 * @author Thomas
 *
 */
public class PhysicianFactory extends HumanFactory {

	public PhysicianFactory() {
		super();
	}
	
	/**
	 * Create a physician with random name and surname
	 * @return a physician
	 */
	public Physician create(EmergencyDepartment emergencyDepartment) {
		return new Physician(getRandomName(), getRandomSurname(), emergencyDepartment);
		}

	
}
