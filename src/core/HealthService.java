package core;

import java.util.ArrayList;

/**
 * HealthService is an abstract class which represents every health service in the Emergency Department.
 * A Health Service gives a special treatment depending on its role. It has a cost.
 * It has a waiting queue of patients and apply the treatment to the first one.
 * It takes a random time given by a probability distribution.
 * Once the treatment is over, the physician in charge of the patient is notified.
 * @author Quentin
 *
 */

public abstract class HealthService extends Entity {
	protected ArrayList<Patient> waitingQueue;
	protected ProbabilityDistribution durationProbability;
	protected Double cost;
	
	public HealthService(String name, ProbabilityDistribution durationProbability,
			Double cost) {
		super(name);
		this.waitingQueue = new ArrayList<Patient>();
		this.durationProbability = durationProbability;
		this.cost = cost;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
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
	 * This method applies the service to the next patient. Once done, the physician is notified.
	 * @param patient
	 */
	public void executeServiceOnPatient(Patient patient) {
		
	}
	
	
}
