package workflow;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;
import resources.Room;
import resources.Transporter;
import utils.ProbabilityDistribution;

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
	 * This method overrides canTreatPatient of WorkflowElement.
	 * It checks if there is an idle transporter and an available room for examining the patient.
	 * @param patient A Patient instance
	 * @return boolean: true if the patient can be treated by the service, false otherwise
	 * @see WorkflowElement#canTreatPatient
	 */
	@Override
	public boolean canTreatPatient(Patient patient) {
		return (patient != null);
	}
	
	@Override
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		this.waitingQueue.remove(patient);
		Event beginService = new Event(this.name + " beginning", emergencyDepartment.getTime());
		patient.addEvent(beginService);
		patient.setState("taking-" + this.name);
		this.generateEndTask(this, patient);
	}

	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endService = new Event(this.name + " ending", emergencyDepartment.getTime());
		patient.addEvent(endService);
		patient.addCharges(cost);
		patient.getLocation().removePatient(patient);
		patient.setLocation(null);
		patient.setState("waiting");
		emergencyDepartment.getService("Installation").addPatientToWaitingList(patient);
	}
	
	
//	/**
//	 * This method overrides executeServiceOnPatient of WorkflowElement.
//	 * It checks if there is a transporter and a room available for the health service.
//	 * If so, it sends the patient to the service.
//	 * Then it adds the transport, beginning and end of the service events to the patient's history.
//	 * The method is built to work independently of the type of the health service.
//	 */
//	
//	@Override
//	public void executeServiceOnPatient(Patient patient) {
//		// TODO Auto-generated method stub
//		Transporter transporter = emergencyDepartment.getIdleTransporter();
//		if(transporter != null) {
//			Room healthServiceRoom = emergencyDepartment.getAvailableRoom(this.getName().concat("Room"));
//			if(healthServiceRoom != null) {
//				((Transportation) emergencyDepartment.getService("Transportation")).transportPatient(transporter, patient);
//				patient.setLocation(healthServiceRoom);
//				Double beginning = patient.getHistoryTime();
//				Double duration = this.durationProbability.generateSample();
//				Event serviceBeginning = new Event(this.getName().concat(" beginning"), beginning);
//				Event serviceEnding = new Event(this.getName().concat(" ending"), beginning + duration);
//				patient.addEvent(serviceBeginning);
//				patient.addEvent(serviceEnding);
//				patient.addCharges(cost);
//				patient.notifyObservers();
//				emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
//			}
//		}
//		else {
//			emergencyDepartment.getService(this.getName()).addPatientToWaitingList(patient);
//		}
//		
//	}

}
