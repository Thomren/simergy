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
	 * This is a method overriding executeServiceOnPatient of WorkflowElement.
	 * It checks if there is a nurse and an available room (shock or box room) to take care of the patient.
	 * If so, it sends him to the appropriate room for consultation. The history of the patient is then updated.
	 * Else, it is sent back to the waiting queue.
	 * 
	 * @param patient
	 */
	
	
	
//	@Override
//	public void executeServiceOnPatient(Patient patient) {
//		// TODO Auto-generated method stub
//		Nurse nurse = emergencyDepartment.getIdleNurse();
//		if(nurse != null) {
//			if(patient.getSeverityLevel().getLevel() <= 2) {
//				Room shockRoom = emergencyDepartment.getAvailableRoom("ShockRoom");
//				if(shockRoom != null) {
//					Event registration = new Event("Registration", patient.getHistoryTime());
//					patient.addEvent(registration);
//					patient.addCharges(cost);
//					((Installation) emergencyDepartment.getService("Installation")).installPatient(nurse, patient);
//					patient.setLocation(shockRoom);
//					emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
//				}
//				else {
//					emergencyDepartment.getService("Triage").addPatientToWaitingList(patient);
//				}
//			}
//			else {
//				Room boxRoom = emergencyDepartment.getAvailableRoom("BoxRoom");
//				if(boxRoom != null) {
//					Event registration = new Event("Registration", patient.getHistoryTime());
//					patient.addEvent(registration);
//					patient.addCharges(cost);
//					((Installation) emergencyDepartment.getService("Installation")).installPatient(nurse, patient);
//					patient.setLocation(boxRoom);
//					emergencyDepartment.getService("Consultation").addPatientToWaitingList(patient);
//				}
//				else {
//					emergencyDepartment.getService("Triage").addPatientToWaitingList(patient);
//				}
//			}
//		}
//		else {
//			emergencyDepartment.getService("Triage").addPatientToWaitingList(patient);
//		}
//	}

	@Override
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Nurse nurse = emergencyDepartment.getIdleNurse();
		if(nurse != null) {
			Event beginRegistration = new Event("Registration beginning", emergencyDepartment.getTime());
			patient.addEvent(beginRegistration);
			nurse.setState("occupied");
			Double endTimestamp = emergencyDepartment.getTime()+this.durationProbability.generateSample();
			Task endTriage = new Task(endTimestamp, new EndService(this, patient, nurse));
			emergencyDepartment.getTasksQueue().addTask(endTriage);
		}
		else {
			Double nextAvailableNurseTime = this.getNextAvailableEmployeeTime("Nurse");
			Task startNewTriage = new Task(nextAvailableNurseTime, new StartService(this, patient));
			emergencyDepartment.getTasksQueue().addTask(startNewTriage);
			emergencyDepartment.getService("Triage").addPatientToWaitingList(patient);
		}
		
	}

	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		Event endRegistration = new Event("Registration ending", emergencyDepartment.getTime());
		patient.addEvent(endRegistration);
		patient.addCharges(cost);
		Task beginTransportation = new Task(emergencyDepartment.getTime(), new StartService(this.emergencyDepartment.getService("Transportation"), patient));
		emergencyDepartment.getTasksQueue().addTask(beginTransportation);
	}

}
