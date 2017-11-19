package workflow;

import java.util.Iterator;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import processing.Command;
import processing.EndService;
import processing.StartService;
import processing.Task;
import processing.TasksQueue;
import resource.BoxRoom;
import resource.Nurse;
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
	 * This method overrides canTreatPatient of WorkflowElement.
	 * It checks if there is an available nurse for registering the patient.
	 * @param patient
	 * @return boolean: true if the patient can be treated by the service, false otherwise
	 * @see WorkflowElement.canTreatPatient
	 */
	@Override
	public boolean canTreatPatient(Patient patient) {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		return patient != null & nurse != null;
	}

	/**
	 * This method overrides startServiceOnPatient of WorkflowElement.
	 * It finds a nurse for the patient registration.
	 * It updates the patient, and the nurse information.
	 * Then it adds the end of the registration to the service task queue.
	 * @param patient
	 */
	@Override
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		nurse.setState("occupied");
		Event beginRegistration = new Event("Registration beginning", emergencyDepartment.getTime());
		patient.addEvent(beginRegistration);
		patient.setState("being-registrated");
		this.generateEndTask(this, patient, nurse);
	}

	/**
	 * This method overrides endServiceOnPatient of WorkflowElement.
	 * It ends the registration of a patient before sending him to the waiting queue of installation.
	 * First it updates the patient and the nurse information.
	 * Then it adds the patient to the installation waiting queue.
	 * @param patient
	 */
	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endRegistration = new Event("Registration ending", emergencyDepartment.getTime());
		patient.addEvent(endRegistration);
		patient.addCharges(cost);
		removePatientFromWaitingList(patient);
		patient.setState("waiting");
		emergencyDepartment.getService("Transportation").addPatientToWaitingList(patient);
	}

}
