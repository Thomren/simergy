package processing;

import resource.Patient;
import workflow.WorkflowElement;

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

	
	
}
