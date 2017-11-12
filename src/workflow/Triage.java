package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.BoxRoom;
import resource.Patient;
import resource.Room;
import resource.ShockRoom;

/**
 * This is a class extending WorkflowElement. It represents the triage step.
 * The incoming patients are sent to different rooms, depending on their severity level.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Triage extends WorkflowElement {

	public Triage(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment, Double cost) {
		super(name, durationProbability, cost, emergencyDepartment);
	}
	
	/**
	 * This is a method overriding executeServiceOnPatient of WorkflowElement.
	 * It checks if there is a nurse and an available room (shock or box room) to take care of the patient.
	 * If so, it sends him to the appropriate room for consultation. The history of the patient is then updated.
	 * Else, it is sent back to the waiting queue.
	 * 
	 * @param patient
	 */
	
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		if(emergencyDepartment.getIdleNurse() != null) {
			if(patient.getSeverityLevel().getLevel() <= 2) {
				Room shockRoom = emergencyDepartment.getAvailableRoom("ShockRoom");
				if(shockRoom != null) {
					Event registration = new Event("registration", patient.getHistoryTime());
					patient.addEvent(registration);
					emergencyDepartment.getService("installation").executeServiceOnPatient(patient);
					patient.setLocation(shockRoom);
					emergencyDepartment.getService("consultation").addPatientToWaitingList(patient);
				}
				else {
					emergencyDepartment.getService("triage").addPatientToWaitingList(patient);
				}
			}
			else {
				Room boxRoom = emergencyDepartment.getAvailableRoom("BoxRoom");
				if(boxRoom != null) {
					Event registration = new Event("registration", patient.getHistoryTime());
					patient.addEvent(registration);
					emergencyDepartment.getService("installation").executeServiceOnPatient(patient);
					patient.setLocation(boxRoom);
					emergencyDepartment.getService("consultation").addPatientToWaitingList(patient);
				}
				else {
					emergencyDepartment.getService("triage").addPatientToWaitingList(patient);
				}
			}
		}
		else {
			emergencyDepartment.getService("triage").addPatientToWaitingList(patient);
		}
	}

}
