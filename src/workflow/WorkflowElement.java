package workflow;

import java.util.ArrayList;

import core.Entity;
import core.ProbabilityDistribution;
import resource.Patient;

/**
 * This is an abstract class which represents every workflow element in the Emergency Department.
 * A workflow element represent a special task.
 * It has a waiting queue of patients and apply the treatment to the first one.
 * It takes a random time given by a probability distribution.
 * @author Quentin
 *
 */

public abstract class WorkflowElement extends Entity {
	protected ArrayList<Patient> waitingQueue;
	protected ProbabilityDistribution durationProbability;
	
	public WorkflowElement(String name, ProbabilityDistribution durationProbability) {
		super(name);
		this.waitingQueue = new ArrayList<Patient>();
		this.durationProbability = durationProbability;
	}
	
	/**
	 * This method adds the incoming patient to the waiting queue of patients.
	 * @param patient
	 */
	public void addPatientToWaitingList(Patient patient) {
		this.waitingQueue.add(patient);
	}
	
	/**
	 * This method applies the service to the next patient of the waiting queue.
	 * @param patient
	 */
	public abstract void executeServiceOnPatient(Patient patient);
}
