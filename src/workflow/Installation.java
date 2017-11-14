package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Nurse;
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

	public void installPatient(Nurse nurse, Patient patient) {
		// TODO Auto-generated method stub
		Double installationBeginningTime = emergencyDepartment.getTime();
		Event installationBeginning = new Event("Installation beginning", installationBeginningTime);
		System.out.println(installationBeginning);
		Event installationEnding = new Event("Installation ending", installationBeginningTime + this.installationTime);
		patient.addEvent(installationBeginning);
		patient.addEvent(installationEnding);
		System.out.println(nurse);
		nurse.addEvent(installationBeginning);
		nurse.addEvent(installationEnding);
		patient.addCharges(cost);
		
	}

	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		
	}

}
