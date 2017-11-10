package workflow;

import core.ProbabilityDistribution;

/**
 * This is an abstract class, extending WorkflowElement, which represents every Health Service in the Emergency Department.
 * An Health Service represent a special treatment depending on its role. It has a cost.
 * Once the treatment is over, the physician in charge of the patient is notified.
 * @author Quentin
 *
 */

public abstract class HealthService extends WorkflowElement {
	protected Double cost;

	public HealthService(String name, ProbabilityDistribution durationProbability, Double cost) {
		super(name, durationProbability);
		this.cost = cost;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	

}
