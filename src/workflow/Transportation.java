package workflow;

import core.EmergencyDepartment;
import core.ProbabilityDistribution;

/**
 * This is a class extending WorkflowElement. It represents the transportation to test step.
 * The patients are sent to the test rooms corresponding to their test needs.
 * The step requires a transporter.
 * @author Quentin
 *
 */

public class TransportationToTest extends WorkflowElement {

	public TransportationToTest(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

}
