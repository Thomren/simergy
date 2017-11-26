package workflow;

import java.util.ArrayList;
import core.EmergencyDepartment;
import core.Entity;
import processing.EndService;
import processing.StartService;
import processing.Task;
import processing.TasksQueue;
import resources.Human;
import resources.Patient;
import resources.Room;
import utils.ProbabilityDistribution;

/**
 * This is an abstract class which represents every workflow element in the Emergency Department.
 * A workflow element represent a special task.
 * It has a waiting queue of patients and apply the treatment to the first one.
 * It also has a tasks queue for tasks which has to be terminated in the future.
 * It has a cost.
 * It may take a random time given by a probability distribution.
 * @author Quentin
 *
 */

public abstract class WorkflowElement extends Entity {
	protected ArrayList<Patient> waitingQueue;
	protected ProbabilityDistribution durationProbability;
	protected Double cost;
	protected TasksQueue tasksQueue;
	
	public WorkflowElement(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.waitingQueue = new ArrayList<Patient>();
		this.durationProbability = durationProbability;
		this.cost = cost;
		this.tasksQueue = new TasksQueue();
	}
	
	/**
	 * This method adds the incoming patient to the waiting queue of patients.
	 * @param patient A Patient instance
	 */
	public void addPatientToWaitingList(Patient patient) {
		this.waitingQueue.add(patient);
	}
	
	/**
	 * This method remove a patient from the waiting queue of patients.
	 * @param patient A Patient instance
	 */
	public void removePatientFromWaitingList(Patient patient) {
		this.waitingQueue.remove(patient);
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
	
	public Patient getNextSeverePatient() {
		for (Patient patient : waitingQueue) {
			if (patient.getSeverityLevel().getLevel() < 3) {
				return patient;
			}
		}
		return null;
	}
	
	public Patient getNextLightPatient() {
		for (Patient patient : waitingQueue) {
			if (patient.getSeverityLevel().getLevel() >= 3) {
				return patient;
			}
		}
		return null;
	}
	
	/**
	 * This method begin the application of the service to a patient.
	 * @param patient A Patient instance
	 */
	public abstract void startServiceOnPatient(Patient patient);
	
	/**
	 * This method end the service for a patient.
	 * @param patient A Patient instance
	 */
	public abstract void endServiceOnPatient(Patient patient);
	
	/**
	 * This method return the next task to be executed by the service
	 * The priority in the tasks is the following:
	 * <ul>
	 * <li>Treat the next severe patient (L1 or L2)</li>
	 * <li>Treat the next light patient (L3 to L5)</li>
	 * <li>End the treatment of the next patient in the tasks queue</li>
	 * </ul>
	 * @return The next task to be done by the installation service
	 */
	public Task getNextTask() {
		Patient severePatient = this.getNextSeverePatient();
		Patient lightPatient = this.getNextLightPatient();
		if (this.canTreatPatient(severePatient)) {
			return new Task(this.emergencyDepartment.getTime(), new StartService(this, severePatient));			
		}
		else if (this.canTreatPatient(lightPatient)) {
			return new Task(this.emergencyDepartment.getTime(), new StartService(this, lightPatient));				
		}
		else {
			return this.tasksQueue.getNextTask();
		}
	}
	
	/**
	 * This abstract method determine if the given patient can be treated by the service.
	 * The requirements depend on the service.
	 * @param patient A Patient instance
	 * @return boolean: true if the patient can be treated, false otherwise
	 */
	public abstract boolean canTreatPatient(Patient patient);
	
	public void generateEndTask(WorkflowElement workflowElement, Patient patient) {
		Double endTimestamp = emergencyDepartment.getTime() + workflowElement.durationProbability.generateSample();
		Task endTask = new Task(endTimestamp, new EndService(workflowElement, patient));
		this.getTasksQueue().addTask(endTask);
	}
	
	public void generateEndTask(WorkflowElement workflowElement, Patient patient, Human employee) {
		Double endTimestamp = emergencyDepartment.getTime() + workflowElement.durationProbability.generateSample();
		Task endTask = new Task(endTimestamp, new EndService(workflowElement, patient, employee));
		this.getTasksQueue().addTask(endTask);
	}
	
	public void generateEndTask(WorkflowElement workflowElement, Patient patient, Human employee, Room room) {
		Double endTimestamp = emergencyDepartment.getTime() + workflowElement.durationProbability.generateSample();
		Task endTask = new Task(endTimestamp, new EndService(workflowElement, patient, employee, room));
		this.getTasksQueue().addTask(endTask);
	}
	
//	public Double getNextAvailableEmployeeTime(String className) {
//		Iterator<Task> iteratorTasksQueue = this.emergencyDepartment.getTasksQueue().getQueue().iterator();
//		Double nextAvailableEmployeeTime = Double.POSITIVE_INFINITY;
//		while (iteratorTasksQueue.hasNext()) {
//			Task task = (Task) iteratorTasksQueue.next();
//			Command commandTask = task.getCommand();
//			if (commandTask instanceof EndService) {
//				EndService endServiceTask = (EndService) commandTask;
//				if (endServiceTask.getEmployee().getClass().getName() == className) {
//					nextAvailableEmployeeTime = Math.min(nextAvailableEmployeeTime, task.getTimestamp());
//				}
//			}
//		}
//		return nextAvailableEmployeeTime;
//	}
//	
//	/**
//	 * This method handle the next patient of the service.
//	 * @see getNextPatient
//	 * @see executeServiceOnPatient
//	 */
//	public void handleNextPatient() {
//		try {
//			this.executeServiceOnPatient(this.getNextPatient());
//		}
//		catch(IndexOutOfBoundsException e) {
//			// Do nothing
//		}
//	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
		
	public ArrayList<Patient> getWaitingQueue() {
		return waitingQueue;
	}

	public void setWaitingQueue(ArrayList<Patient> waitingQueue) {
		this.waitingQueue = waitingQueue;
	}

	public ProbabilityDistribution getDurationProbability() {
		return durationProbability;
	}

	public void setDurationProbability(ProbabilityDistribution durationProbability) {
		this.durationProbability = durationProbability;
	}

	public TasksQueue getTasksQueue() {
		return tasksQueue;
	}

	public void setTasksQueue(TasksQueue tasksQueue) {
		this.tasksQueue = tasksQueue;
	}
	
	public void removeNextTask() {
		this.tasksQueue.getQueue().poll();
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String toStringDetailed() {
		return toString() + " (Cost: " + cost + ")\n    Waiting List : " + waitingQueue;
	}
	
}
