package processing;

import core.EmergencyDepartment;
import resource.Patient;

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

}
