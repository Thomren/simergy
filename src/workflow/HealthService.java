package workflow;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import resource.Patient;
import resource.Room;

/**
 * This is an abstract class, extending WorkflowElement, which represents every Health Service in the Emergency Department.
 * An Health Service represent a special treatment depending on its role. It has a cost.
 * Once the treatment is over, the physician in charge of the patient is notified.
 * @author Quentin
 *
 */

public abstract class HealthService extends WorkflowElement {
	protected Double cost;

	public HealthService(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, emergencyDepartment);
		this.cost = cost;
	}
		
	@Override
	public void executeServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		if(emergencyDepartment.getIdleTransporter() != null) {
			Room healthServiceRoom = emergencyDepartment.getAvailableRoom(this.getName().concat("Room"));
			if(healthServiceRoom != null) {
				patient.setLocation(healthServiceRoom);
				Event serviceBeginning = new Event(this.getName(), 0.0);
				patient.addEvent(serviceBeginning);
			}
		}
		else {
			// put the patient in the health service queue
		}
		
	}
	
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	

}
