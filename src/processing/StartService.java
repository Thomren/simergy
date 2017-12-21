package processing;

import resources.Patient;
import workflow.WorkflowElement;

/**
 * This class represents the command corresponding to the start of a service on a patient
 * @author Thomas
 *
 */
public class StartService implements Command {
	protected WorkflowElement service;
	protected Patient patient;
	
	public StartService(WorkflowElement service, Patient patient) {
		super();
		this.service = service;
		this.patient = patient;
	}

	@Override
	public void execute() {
		service.startServiceOnPatient(patient);
	}

	public WorkflowElement getService() {
		return service;
	}

	public void setService(WorkflowElement service) {
		this.service = service;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@Override
	public String toString() {
		return "Start of service " + service + " on " + patient;
	}
	
}
