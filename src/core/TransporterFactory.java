package core;

import resource.Transporter;

/**
 * Factory to generate nurses
 * @author Thomas
 *
 */
public class TransporterFactory extends HumanFactory {

	public TransporterFactory() {
		super();
	}
	
	/**
	 * Create a transporter with random name and surname
	 * @return a transporter
	 */
	public Transporter create(EmergencyDepartment emergencyDepartment) {
		return new Transporter(getRandomName(), getRandomSurname(), emergencyDepartment);
		}

	
}
