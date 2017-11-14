package workflow;

import java.util.Random;

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
	private final Double noExamRate = 0.4;
	private final Double bloodTestRate = 0.35;
	private final Double xRayRate = 0.2;
	private final Double mRIRate = 0.05;
	

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
		if(patient.getPhysician() != null) {
			Physician physician = patient.getPhysician();
			if (physician.getState() == "idle") {
				this.examinePatient(physician, patient);
			}
			else {
				emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
			}
		}
		else {
			Physician physician = emergencyDepartment.getIdlePhysician();
			if(physician != null) {
				physician.addOverseenPatient(patient);
				this.examinePatient(physician, patient);
			}
			else {
				emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
			}
		}
	}

	private String determineExamination() {
		// TODO Auto-generated method stub
		Random random = new Random();
		Double exam = random.nextDouble();
		if(exam < noExamRate) {
			return "Release";
		}
		else if (exam < (noExamRate + bloodTestRate)) {
			return "BloodTest";
		}
		else if (exam < (noExamRate + bloodTestRate + xRayRate)) {
			return "XRay";
		}
		else {
			return "MRI";
		}
	}
	
	public void examinePatient(Physician physician, Patient patient) {
		String examination = this.determineExamination();
		Event consultationBeginning = new Event("Consultation beginning", patient.getHistoryTime());
		Event consultationEnd = new Event("Consultation ending : ".concat(examination), patient.getHistoryTime() + this.durationProbability.generateSample());
		patient.addEvent(consultationBeginning);
		patient.addEvent(consultationEnd);
		physician.addEvent(consultationBeginning);
		physician.addEvent(consultationEnd);
		patient.addCharges(cost);
		if(examination != "Release") {
			emergencyDepartment.getService(examination).addPatientToWaitingList(patient);
		}
		else {
			physician.removeOverseenPatient(patient);
			physician.addTreatedPatient(patient);
			emergencyDepartment.removePatient(patient);
		}
	}


}
