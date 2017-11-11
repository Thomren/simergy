package workflow;

import core.EmergencyDepartment;
import core.ProbabilityDistribution;

/**
 * This is a class extending HealthService. It represent the consultation service.
 * Patients came here to see a Physician. The physician determine if they have to pass a medical test.
 * If so, the physician send them to the appropriate service thanks to TransportToTest.
 * The proportions are the following:
 * <ul>
 * <li>No test: 35%</li>
 * <li>Blood test: 40%</li>
 * <li>X Ray test: 20%</li>
 * <li>MRI test: 5%</li>
 * </ul>
 * It requires a Physician.
 * @author Quentin
 *
 */

public class Consultation extends HealthService {

	public Consultation(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

}
