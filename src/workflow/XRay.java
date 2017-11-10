package workflow;

import core.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the XRay service.
 * Patients who has to pass a XRay use this service.
 * It requires a XRay room.
 * @author Quentin
 *
 */

public class XRay extends HealthService {

	public XRay(String name, ProbabilityDistribution durationProbability, Double cost) {
		super(name, durationProbability, cost);
		// TODO Auto-generated constructor stub
	}

}
