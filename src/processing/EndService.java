package processing;

import resource.Human;
import resource.Patient;
import resource.Room;
import workflow.WorkflowElement;

public class EndService implements Command {
	protected WorkflowElement service;
	protected Patient patient;
	protected Human employee;
	protected Room room;
	
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
	
	public EndService(WorkflowElement service, Patient patient, Human employee, Room room) {
		super();
		this.service = service;
		this.patient = patient;
		this.employee = employee;
		this.room = room;
	}



	@Override
	public void execute() {
		service.endServiceOnPatient(patient);
		if (this.employee != null) {
			this.employee.setState("idle");
		}
		if (this.room != null) {
			this.patient.setLocation(room);
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
