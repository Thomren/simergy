package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;

/**
 * This is a class extending WorkflowElement. It represents the transportation to test step.
 * The patients are sent to the test rooms corresponding to their test needs.
 * The step requires a transporter.
 * @author Quentin
 *
 */

public class Transportation extends WorkflowElement {
	protected final Double transportTime = 5.0;
	
	public Transportation(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment, Double cost) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Double transportBeginningTime = emergencyDepartment.getTime();
		Event transportBeginning = new Event("TransportBeginning", transportBeginningTime);
		Event transportEnding = new Event("TransportEnding", transportBeginningTime + this.transportTime);
		patient.addEvent(transportBeginning);
		patient.addEvent(transportEnding);
	}

}
