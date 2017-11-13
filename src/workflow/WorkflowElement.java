package workflow;

import java.util.ArrayList;

import core.EmergencyDepartment;
import core.Entity;
import core.ProbabilityDistribution;
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
	 * This method applies the service to the next patient of the waiting queue.
	 * @param patient
	 */
	public abstract void executeServiceOnPatient(Patient patient);
	
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
