package core;

import processing.PatientArrival;
import resources.Patient;
import utils.SeverityLevel;

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
	 * @param severityLevel A SeverityLevel instance
	 * @param emergencyDepartment An EmergencyDepartment instance
	 * @return a patient
	 */
	public Patient create(SeverityLevel severityLevel, EmergencyDepartment emergencyDepartment) {
		return new Patient(getRandomName(), getRandomSurname(), getRandomInsurance(), severityLevel, emergencyDepartment);
	}
	
	public Patient create(EmergencyDepartment emergencyDepartment) {
		return ((PatientArrival) emergencyDepartment.getNextPatientArrival().getCommand()).getPatient();
	}

}
