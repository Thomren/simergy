package workflow;

import core.EmergencyDepartment;
import core.ProbabilityDistribution;
import resource.Patient;

/**
 * This is a class extending WorkflowElement. It represents the triage step.
 * The incoming patients are sent to different rooms, depending on their severity level.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Triage extends WorkflowElement {

	public Triage(String name, ProbabilityDistribution durationProbability) {
		super(name, durationProbability);
	}
	
	/**
	 * This is a method overriding executeServiceOnPatient of WorkflowElement.
	 * It checks if there is a nurse and an available room (shock or box room) to take care of the patient.
	 * If so, it sends him to the appropriate room for consultation.
	 * Else, it is sent back to the waiting queue.
	 * 
	 * @param patient
	 */
	
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		if(emergencyDepartment.getIdleNurse() != null) {
			if(patient.getSeverityLevel().getLevel() <= 2) {
				shockRoom = emergencyDepartment.getAvailableRoom(ShockRoom);
				if(shockRoom != null) {
				patient.setLocation(shockRoom);
				emergencyDepartment.getServices().consultation.addPatient(patient);
				}
				else {
					emergencyDepartment.getServices().triage.addPatient(patient);
				}
			}
			else {
				boxRoom = emergencyDepartment.getAvailableBoxRoom();
				if(boxRoom != null) {
				patient.setLocation(boxRoom);
				emergencyDepartment.getServices().consultation.addPatient(patient);
				}
				else {
					emergencyDepartment.getServices().triage.addPatient(patient);
				}
			}
		}
		else {
			emergencyDepartment.getServices().triage.addPatient(patient);
		}
		
		
	}

}
