package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resource.Nurse;
import resource.Patient;
import resource.Room;

/**
 * This is a class extending WorkflowElement. It represents the installation in the consultation room.
 * The patients are sent to the room the triage assigned them.
 * The step requires a nurse.
 * @author Quentin
 *
 */

public class Installation extends WorkflowElement {
	protected final Double installationTime = 2.0;

	public Installation(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment, Double cost) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

	public void installPatient(Nurse nurse, Patient patient) {
		// TODO Auto-generated method stub
		Double installationBeginningTime = emergencyDepartment.getTime();
		Event installationBeginning = new Event("Installation beginning", installationBeginningTime);
		System.out.println(installationBeginning);
		Event installationEnding = new Event("Installation ending", installationBeginningTime + this.installationTime);
		patient.addEvent(installationBeginning);
		patient.addEvent(installationEnding);
		System.out.println(nurse);
		nurse.addEvent(installationBeginning);
		nurse.addEvent(installationEnding);
		patient.addCharges(cost);
		
	}
	
	/**
	 * This method overrides getNextTask of WorkflowElement.
	 * The priority in the tasks is the following:
	 * <ul>
	 * <li>Begin installation of severe patient (L1 or L2)</li>
	 * <li>Begin installation of light patient (L3 to L5)</li>
	 * <li>End installation of the next patient in the tasks queue</li>
	 * </ul>
	 * @return The next task to be done by the installation service
	 */
	@Override
	public Task getNextTask() {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		if (nurse != null & emergencyDepartment.getAvailableRoom("ShockRoom") != null) {
			Patient patient = this.getNextSeverePatient();
			return new Task(this.emergencyDepartment.getTime(), new StartService(this, patient));				
		}
		else if (nurse != null & emergencyDepartment.getAvailableRoom("BoxRoom") != null) {
			Patient patient = this.getNextLightPatient();
			return new Task(this.emergencyDepartment.getTime(), new StartService(this, patient));				
		}
		else {
			return this.tasksQueue.getNextTask();
		}
	}

	/**
	 * This method overrides startServiceOnPatient of WorkflowElement.
	 * It finds a nurse and a room for the patient installation.
	 * It updates the patient, the nurse, and the room information.
	 * Then it adds the end of the task to the service task queue.
	 * @param patient
	 */
	@Override
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		nurse.setState("occupied");
		Room room;
		if(patient.getSeverityLevel().getLevel() <= 2) {
			room = emergencyDepartment.getAvailableRoom("ShockRoom");
			room.addPatient(patient);
		}
		else {
			room = emergencyDepartment.getAvailableRoom("ShockRoom");
			room.addPatient(patient);
		}
		Event beginTransportation = new Event("Transportation beginning", emergencyDepartment.getTime());
		patient.addEvent(beginTransportation);
		patient.setLocation(null);
		patient.setState("being-installed");
		Double endTimestamp = emergencyDepartment.getTime() + this.durationProbability.generateSample();
		Task endTransportation = new Task(endTimestamp, new EndService(this, patient, nurse, room));
		emergencyDepartment.getTasksQueue().addTask(endTransportation);
	}
	
	/**
	 * This method overrides endServiceOnPatient of WorkflowElement.
	 * It ends the installation of a patient before sending him to the waiting queue of consultation.
	 * First it updates the patient, the nurse, and the room information.
	 * Then it adds the patient to the consultation waiting queue.
	 * @param patient
	 */
	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endTransportation = new Event("Transport ending", emergencyDepartment.getTime());
		patient.addEvent(endTransportation);
		patient.addCharges(cost);
		emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
	}

}
