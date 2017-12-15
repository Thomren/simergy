package workflow;

import core.EmergencyDepartment;
import core.Event;
import resources.Nurse;
import resources.Patient;
import resources.Room;
import utils.ProbabilityDistribution;

/**
 * This is a class extending WorkflowElement. It represents the triage step.
 * The incoming patients are sent to different rooms, depending on their severity level.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Triage extends WorkflowElement {

	public Triage(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
	}
	
	public Triage(ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super("Triage", durationProbability, cost, emergencyDepartment);
	}
	
	/**
	 * This method overrides canTreatPatient of WorkflowElement.
	 * It checks if there is an available nurse for registering the patient.
	 * @param patient A Patient instance
	 * @return boolean: true if the patient can be treated by the service, false otherwise
	 * @see WorkflowElement#canTreatPatient
	 */
	@Override
	public boolean canTreatPatient(Patient patient) {
		Nurse nurse = emergencyDepartment.getIdleNurse();
		return patient != null & nurse != null;
	}

	/**
	 * This method overrides startServiceOnPatient of WorkflowElement.
	 * It finds a nurse for the patient registration.
	 * It updates the patient, and the nurse information.
	 * Then it adds the end of the registration to the service task queue.
	 * @param patient A Patient instance
	 */
	@Override
	public void startServiceOnPatient(Patient patient) {
		this.waitingQueue.remove(patient);
		Nurse nurse = emergencyDepartment.getIdleNurse();
		nurse.setState("occupied");
		Event beginRegistration = new Event("Registration beginning", emergencyDepartment.getTime());
		patient.addEvent(beginRegistration);
		patient.setState("being-registered");
		Room room = emergencyDepartment.getAvailableRoom("WaitingRoom");
		room.addPatient(patient);
		this.generateEndTask(this, patient, nurse, room);
	}

	/**
	 * This method overrides endServiceOnPatient of WorkflowElement.
	 * It ends the registration of a patient before sending him to the waiting queue of installation.
	 * First it updates the patient and the nurse information.
	 * Then it adds the patient to the installation waiting queue.
	 * @param patient A Patient instance
	 */
	@Override
	public void endServiceOnPatient(Patient patient) {
		Event endRegistration = new Event("Registration ending", emergencyDepartment.getTime());
		patient.addEvent(endRegistration);
		patient.addCharges(cost);
		patient.setState("waiting");
		emergencyDepartment.getService("Installation").addPatientToWaitingList(patient);
	}

}
