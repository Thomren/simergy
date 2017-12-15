package processing;

import resources.Human;
import resources.Patient;
import resources.Room;
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
		this.room = null;
	}

	public EndService(WorkflowElement service, Patient patient, Human employee) {
		super();
		this.service = service;
		this.patient = patient;
		this.employee = employee;
		this.room = null;
	}
	
	public EndService(WorkflowElement service, Patient patient, Room room) {
		super();
		this.service = service;
		this.patient = patient;
		this.employee = null;
		this.room = room;
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
		service.removeNextTask();
		if (this.employee != null) {
			this.employee.setState("idle");
		}
		if (this.room != null) {
			this.patient.getLocation().removePatient(patient);
			this.patient.setLocation(room);
		}
		service.endServiceOnPatient(patient);
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

	@Override
	public String toString() {
		StringBuffer content = new StringBuffer();
		content.append("End of service " + service + " on " + patient );
		if(employee != null) {
			content.append(" by " + employee);
		}
		if(room != null) {
			content.append(" in " + room);
		}
		return content.toString();
	}

}
