package workflow;

import java.util.ArrayList;
import java.util.Iterator;

import core.EmergencyDepartment;
import core.Entity;
import core.ProbabilityDistribution;
import processing.Command;
import processing.EndService;
import processing.Task;
import resource.Patient;

/**
 * This is an abstract class which represents every workflow element in the Emergency Department.
 * A workflow element represent a special task.
 * It has a waiting queue of patients and apply the treatment to the first one.
 * It has a cost.
 * It may take a random time given by a probability distribution.
 * @author Quentin
 *
 */

public abstract class WorkflowElement extends Entity {
	protected ArrayList<Patient> waitingQueue;
	protected ProbabilityDistribution durationProbability;
	protected Double cost;
	
	public WorkflowElement(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.waitingQueue = new ArrayList<Patient>();
		this.durationProbability = durationProbability;
		this.cost = cost;
	}
	
	/**
	 * This method adds the incoming patient to the waiting queue of patients.
	 * @param patient
	 */
	public void addPatientToWaitingList(Patient patient) {
		this.waitingQueue.add(patient);
	}
	
	
	/**
	 * This method return the next patient to be treated by the service.
	 * It is selected in the waiting queue depending on the algorithm used.
	 * Currently we use a FIFO algorithm.
	 * @return Patient
	 */
	public Patient getNextPatient() {
		return this.waitingQueue.remove(0);
	}
	
	/**
	 * This method begin the application of the service to a patient.
	 * @param patient
	 */
	public abstract void startServiceOnPatient(Patient patient);
	
	/**
	 * This method end the service for a patient.
	 * @param patient
	 */
	public abstract void endServiceOnPatient(Patient patient);
	
	/**
	 * This method return the next task to be executed by the service
	 */
	public abstract Task getNextTask();
	
	public Double getNextAvailableEmployeeTime(String className) {
		Iterator<Task> iteratorTasksQueue = this.emergencyDepartment.getTasksQueue().getQueue().iterator();
		Double nextAvailableEmployeeTime = Double.POSITIVE_INFINITY;
		while (iteratorTasksQueue.hasNext()) {
			Task task = (Task) iteratorTasksQueue.next();
			Command commandTask = task.getCommand();
			if (commandTask instanceof EndService) {
				EndService endServiceTask = (EndService) commandTask;
				if (endServiceTask.getEmployee().getClass().getName() == className) {
					nextAvailableEmployeeTime = Math.min(nextAvailableEmployeeTime, task.getTimestamp());
				}
			}
		}
		return nextAvailableEmployeeTime;
	}
	
	/**
	 * This method handle the next patient of the service.
	 * @see getNextPatient
	 * @see executeServiceOnPatient
	 */
	public void handleNextPatient() {
		try {
			this.executeServiceOnPatient(this.getNextPatient());
		}
		catch(IndexOutOfBoundsException e) {
			// Do nothing
		}
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String toStringDetailed() {
		return toString() + " (Cost: " + cost + ")\n    Waiting List : " + waitingQueue;
	}
	
}
