package core;

import resource.Nurse;

/**
 * Factory to generate nurses
 * @author Thomas
 *
 */
public class NurseFactory extends HumanFactory {

	public NurseFactory() {
		super();
	}
	
	/**
	 * Create a nurse with random name and surname and return it
	 * @return a nurse
	 */
	public Nurse create(EmergencyDepartment emergencyDepartment) {
		return new Nurse(getRandomName(), getRandomSurname(), emergencyDepartment);
		}

	
}
