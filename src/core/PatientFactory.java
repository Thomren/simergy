package core;

import resource.Patient;

/**
 * Factory to generate patients
 * @author Thomas
 *
 */
public class PatientFactory extends HumanFactory {

	public PatientFactory() {
		super();
	}
	
	/**
	 * Create a patient with given severity level and emergency department and random name and surnames
	 * @param severityLevel
	 * @param emergencyDepartment
	 * @return a patient
	 */
	public Patient createPatient(SeverityLevel severityLevel, EmergencyDepartment emergencyDepartment) {
		return new Patient(getRandomName(), getRandomSurname(), getRandomInsurance(), severityLevel, emergencyDepartment);
	}

}
