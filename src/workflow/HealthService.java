package workflow;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;
import utils.ProbabilityDistribution;

/**
 * This is an abstract class, extending WorkflowElement, which represents every Health Service in the Emergency Department.
 * An Health Service represent an examination.
 * Once the treatment is over, the physician in charge of the patient is notified.
 * @author Quentin
 *
 */

public abstract class HealthService extends WorkflowElement {

	public HealthService(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
	}
	
	/**
	 * This method overrides canTreatPatient of WorkflowElement.
	 * It checks if there is an idle transporter and an available room for examining the patient.
	 * @param patient A Patient instance
	 * @return boolean: true if the patient can be treated by the service, false otherwise
	 * @see WorkflowElement#canTreatPatient
	 */
	@Override
	public boolean canTreatPatient(Patient patient) {
		return (patient != null);
	}
	
	@Override
	public void startServiceOnPatient(Patient patient) {
		this.waitingQueue.remove(patient);
		Event beginService = new Event(this.name + " beginning", emergencyDepartment.getTime());
		patient.addEvent(beginService);
		patient.setState("taking-" + this.name);
		this.generateEndTask(this, patient);
	}

	@Override
	public void endServiceOnPatient(Patient patient) {
		Event endService = new Event(this.name + " ending", emergencyDepartment.getTime());
		patient.addEvent(endService);
		patient.addCharges(cost);
		try {
			patient.getLocation().removePatient(patient);
		}
		catch (NullPointerException e){
		}
		patient.setLocation(null);
		patient.setState("waiting");
		emergencyDepartment.getService("Installation").addPatientToWaitingList(patient);
	}
}
