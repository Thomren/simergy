package processing;

import resource.Human;
import resource.Patient;
import workflow.WorkflowElement;

public class EndService implements Command {
	protected WorkflowElement service;
	protected Patient patient;
	protected Human employee;
	
	public EndService(WorkflowElement service, Patient patient) {
		super();
		this.service = service;
		this.patient = patient;
		this.employee = null;
	}

	public EndService(WorkflowElement service, Patient patient, Human employee) {
		super();
		this.service = service;
		this.patient = patient;
		this.employee = employee;
	}



	@Override
	public void execute() {
		service.endServiceOnPatient(patient);
		if (this.employee != null) {
			this.employee.setState("idle");
		}
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

	public Human getEmployee() {
		return employee;
	}

	public void setEmployee(Human employee) {
		this.employee = employee;
	}

}
