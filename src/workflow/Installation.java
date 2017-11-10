package workflow;

import core.ProbabilityDistribution;

/**
 * This is a class extending WorkflowElement. It represents the installation in the waiting room.
 * The patients are sent to the room the triage assigned them.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Installation extends WorkflowElement {

	public Installation(String name, ProbabilityDistribution durationProbability) {
		super(name, durationProbability);
		// TODO Auto-generated constructor stub
	}

}
