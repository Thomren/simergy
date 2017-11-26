package processing;

import core.EmergencyDepartment;
import resources.Patient;

public class PatientArrival implements Command {
	protected Patient patient;
	protected EmergencyDepartment emergencyDepartment;

	public PatientArrival(Patient patient, EmergencyDepartment emergencyDepartment) {
		super();
		this.patient = patient;
		this.emergencyDepartment = emergencyDepartment;
	}

	@Override
	public void execute() {
		emergencyDepartment.patientArrival(patient);
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public EmergencyDepartment getEmergencyDepartment() {
		return emergencyDepartment;
	}

	public void setEmergencyDepartment(EmergencyDepartment emergencyDepartment) {
		this.emergencyDepartment = emergencyDepartment;
	}

}
