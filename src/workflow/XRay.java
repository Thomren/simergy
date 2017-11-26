package workflow;

import core.EmergencyDepartment;
import utils.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the XRay service.
 * Patients who has to pass a XRay use this service.
 * It requires a XRay room.
 * @author Quentin
 *
 */

public class XRay extends HealthService {

	public XRay(ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super("XRay", durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

}
