package workflow;

import core.EmergencyDepartment;
import utils.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the MRI service.
 * Patients who has to pass a MRI use this service.
 * It requires a MRI room.
 * @author Quentin
 *
 */

public class MRI extends HealthService {

	public MRI(ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super("MRI", durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

}
