package workflow;

import core.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the MRI service.
 * Patients who has to pass a MRI use this service.
 * It requires a MRI room.
 * @author Quentin
 *
 */

public class MRI extends HealthService {

	public MRI(String name, ProbabilityDistribution durationProbability, Double cost) {
		super(name, durationProbability, cost);
		// TODO Auto-generated constructor stub
	}

}
