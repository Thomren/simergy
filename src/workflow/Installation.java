package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;

/**
 * This is a class extending WorkflowElement. It represents the installation in the waiting room.
 * The patients are sent to the room the triage assigned them.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Installation extends WorkflowElement {
	protected final Double installationTime = 2.0;

	public Installation(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment, Double cost) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Double installationBeginningTime = emergencyDepartment.getTime();
		Event installationBeginning = new Event("InstallationBeginning", installationBeginningTime);
		Event installationEnding = new Event("InstallationEnding", installationBeginningTime + this.installationTime);
		patient.addEvent(installationBeginning);
		patient.addEvent(installationEnding);
		
	}

}
