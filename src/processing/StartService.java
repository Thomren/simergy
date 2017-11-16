package processing;

import resource.Patient;
import workflow.WorkflowElement;

public class StartService implements Command {
	protected WorkflowElement service;
	protected Patient patient;

	@Override
	public void execute() {
		service.endServiceOnPatient(patient);
	}

}
