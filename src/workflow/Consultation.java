package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;
import resource.Physician;
import resource.Room;

/**
 * This is a class extending WorkflowElement. It represent the consultation service.
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

public class Consultation extends WorkflowElement {

	public Consultation(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * This method overrides executeServiceOnPatient of WorkflowElement.
	 * It simulates the consultation of a patient by a physician.
	 * If the patient is already overseen by a physician, he waits for him to be available. 
	 * Else, if there is a physician available, he starts overseeing the patient.
	 * 
	 * Either the physician send the patient to examination, or he releases him from the Emergency Department.
	 * In the latter case, the patient become a treated patient of the physician.
	 */
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Physician physician = emergencyDepartment.getIdlePhysician();
		if(physician != null) {
			if(!physician.getOverseenPatients().contains(patient)) {
				physician.addOverseenPatient(patient);
			}
			HealthService examination = this.determineExamination();
			if(examination != null) {
				emergencyDepartment.getService("examination").addPatientToWaitingList(patient);
				Event consultation = new Event("consultation", 17.0);
				patient.addEvent(consultation);
			}
			else {
				physician.addTreatedPatient(patient);
				emergencyDepartment.removePatient(patient);
			}
		}
		else {
			emergencyDepartment.getService("consultation").addPatientToWaitingList(patient);
		}
	}

	private HealthService determineExamination() {
		// TODO Auto-generated method stub
		return null;
	}


}
