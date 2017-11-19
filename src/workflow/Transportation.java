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
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		if(nurse != null) {
			if(patient.getSeverityLevel().getLevel() <= 2) {
				Room shockRoom = emergencyDepartment.getAvailableRoom("ShockRoom");
				if(shockRoom != null) {
					Event beginTransportation = new Event("Transportation beginning", emergencyDepartment.getTime());
					patient.addEvent(beginTransportation);
					nurse.setState("occupied");
					shockRoom.addPatient(patient);
					patient.setLocation(shockRoom);
					Double endTimestamp = emergencyDepartment.getTime()+this.durationProbability.generateSample();
					Task endTransportation = new Task(endTimestamp, new EndService(this, patient, nurse));
					emergencyDepartment.getTasksQueue().addTask(endTransportation);
				}
				else {
					Double nextAvailablePhysicianTime = this.getNextAvailableEmployeeTime("Physician");
					Task startNewTransportation = new Task(nextAvailablePhysicianTime, new StartService(this, patient));
					emergencyDepartment.getTasksQueue().addTask(startNewTransportation);
					emergencyDepartment.getService("Transportation").addPatientToWaitingList(patient);
				}
			}
			else {
				Room boxRoom = emergencyDepartment.getAvailableRoom("BoxRoom");
				if(boxRoom != null) {
					Event beginTransportation = new Event("Transportation beginning", emergencyDepartment.getTime());
					patient.addEvent(beginTransportation);
					nurse.setState("occupied");
					boxRoom.addPatient(patient);
					patient.setLocation(boxRoom);
					Double endTimestamp = emergencyDepartment.getTime()+this.durationProbability.generateSample();
					Task endTransportation = new Task(endTimestamp, new EndService(this, patient, nurse));
					emergencyDepartment.getTasksQueue().addTask(endTransportation);
				}
				else {
					Double nextAvailablePhysicianTime = this.getNextAvailableEmployeeTime("Physician");
					Task startNewTransportation = new Task(nextAvailablePhysicianTime, new StartService(this, patient));
					emergencyDepartment.getTasksQueue().addTask(startNewTransportation);
					emergencyDepartment.getService("Transportation").addPatientToWaitingList(patient);
				}
			}
		}
		else {
			Double nextAvailableNurseTime = this.getNextAvailableEmployeeTime("Nurse");
			Task startNewTransportation = new Task(nextAvailableNurseTime, new StartService(this, patient));
			emergencyDepartment.getTasksQueue().addTask(startNewTransportation);
			emergencyDepartment.getService("Transportation").addPatientToWaitingList(patient);
		}
		
	}

	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endTransportation = new Event("Transport ending", emergencyDepartment.getTime());
		patient.addEvent(endTransportation);
		patient.addCharges(cost);
		Task beginConsultation = new Task(emergencyDepartment.getTime(), new StartService(this.emergencyDepartment.getService("Consultation"), patient));
		emergencyDepartment.getTasksQueue().addTask(beginConsultation);
	}


}
