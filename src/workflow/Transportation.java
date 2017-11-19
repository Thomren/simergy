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
import resource.ShockRoom;
import resource.Transporter;

/**
 * This is a class extending WorkflowElement. It represents the transportation to test step.
 * The patients are sent to the test rooms corresponding to their test needs.
 * The step requires a transporter.
 * @author Quentin
 *
 */

public class Transportation extends WorkflowElement {
	protected final Double transportTime = 5.0;
	
	public Transportation(String name, ProbabilityDistribution durationProbability, EmergencyDepartment emergencyDepartment, Double cost) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}

	public void transportPatient(Transporter transporter, Patient patient) {
		// TODO Auto-generated method stub
		Double transportBeginningTime = emergencyDepartment.getTime();
		Event transportBeginning = new Event("Transport beginning", transportBeginningTime);
		Event transportEnding = new Event("Transport ending", transportBeginningTime + this.transportTime);
		patient.addEvent(transportBeginning);
		patient.addEvent(transportEnding);
		transporter.addEvent(transportBeginning);
		transporter.addEvent(transportEnding);
		patient.addCharges(cost);
	}
	
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
			room = emergencyDepartment.getAvailableRoom("BoxRoom");
			room.addPatient(patient);
		}
		Event beginTransportation = new Event("Transportation beginning", emergencyDepartment.getTime());
		patient.addEvent(beginTransportation);
		patient.setLocation(null);
		patient.setState("being-transported");
		Double endTimestamp = emergencyDepartment.getTime() + this.durationProbability.generateSample();
		Task endTransportation = new Task(endTimestamp, new EndService(this, patient, nurse, room));
		emergencyDepartment.getTasksQueue().addTask(endTransportation);
	}

	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endTransportation = new Event("Transportation ending", emergencyDepartment.getTime());
		patient.addEvent(endTransportation);
		patient.addCharges(cost);
		patient.setState("waiting");
		emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
	}
}
