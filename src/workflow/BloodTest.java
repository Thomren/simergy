package workflow;

import core.EmergencyDepartment;
import utils.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the Blood test service.
 * Patients who has to pass a Blood test use this service.
 * It requires a BloodTest room.
 * @author Quentin
 *
 */

public class BloodTest extends HealthService {

	public BloodTest(ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super("BloodTest", durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

}
