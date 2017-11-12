package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;
import resource.Room;

/**
 * This is a class extending HealthService. It represent the consultation service.
 * Patients came here to see a Physician. The physician determine if they have to pass a medical test.
 * If so, the physician register them to the appropriate service.
 * The Patient can wait for a transporter in the physician's room. 
 * The proportions of medical tests are the following:
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
	
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		if(emergencyDepartment.getIdlePhysician() != null) {
			HealthService examination = this.determineExamination();
			if(examination != null) {
				emergencyDepartment.getServices().examination.addPatient(patient);
				Event consultation = new Event("consultation", 17.0);
				patient.addEvent(consultation);
				}
			}
			else {
				emergencyDepartment.removePatient(patient);
			}
		}
		else {
			emergencyDepartment.getServices().consultation.addPatient(patient);
		}
	}

	private HealthService determineExamination() {
		// TODO Auto-generated method stub
		return null;
	}


}
