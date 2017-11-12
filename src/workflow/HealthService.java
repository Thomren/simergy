package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;
import resource.Room;

/**
 * This is an abstract class, extending WorkflowElement, which represents every Health Service in the Emergency Department.
 * An Health Service represent an examination.
 * Once the treatment is over, the physician in charge of the patient is notified.
 * @author Quentin
 *
 */

public abstract class HealthService extends WorkflowElement {

	public HealthService(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
	}
	
	/**
	 * This method overrides executeServiceOnPatient of WorkflowElement.
	 * It checks if there is a transporter and a room available for the health service.
	 * If so, it sends the patient to the service.
	 * Then it adds the transport, beginning and end of the service events to the patient's history.
	 * The method is built to work independently of the type of the health service.
	 */
	
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		if(emergencyDepartment.getIdleTransporter() != null) {
			Room healthServiceRoom = emergencyDepartment.getAvailableRoom(this.getName().concat("Room"));
			if(healthServiceRoom != null) {
				emergencyDepartment.getService("Transportation").executeServiceOnPatient(patient);
				patient.setLocation(healthServiceRoom);
				Double beginning = patient.getHistoryTime();
				Double duration = this.durationProbability.generateSample();
				Event serviceBeginning = new Event(this.getName().concat(" beginning"), beginning);
				Event serviceEnding = new Event(this.getName().concat(" beginning"), beginning + duration);
				patient.addEvent(serviceBeginning);
				patient.addEvent(serviceEnding);
				patient.notifyObservers();
				emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
			}
		}
		else {
			emergencyDepartment.getService(this.getName()).addPatientToWaitingList(patient);
		}
		
	}

}
