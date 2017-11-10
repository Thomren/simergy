package workflow;

import core.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the Blood test service.
 * Patients who has to pass a Blood test use this service.
 * It requires a BloodTest room.
 * @author Quentin
 *
 */

public class BloodTest extends HealthService {

	public BloodTest(String name, ProbabilityDistribution durationProbability, Double cost) {
		super(name, durationProbability, cost);
		// TODO Auto-generated constructor stub
	}

}
