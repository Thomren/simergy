package workflow;

import java.util.Random;

import core.EmergencyDepartment;
import core.Event;
import core.ProbabilityDistribution;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resource.Nurse;
import resource.Patient;
import resource.Physician;
import resource.Room;

/**
 * This is a class extending WorkflowElement. It represent the consultation service.
 * Patients came here to see a Physician. The physician determine if they have to pass a medical test.
 * If so, the physician register them to the appropriate service.
 * The Patient can wait for a transporter in the physician's room. 
 * The proportions of medical tests are the following:
 * <ul>
 * <li>No test: 35%</li>
 * <li>Blood test: 40%</li>
 * <li>X Ray test: 20%</li>
 * <li>MRI test: 5%</li>
 * </ul>
 * It requires a Physician.
 * @author Quentin
 *
 */

public class Consultation extends WorkflowElement {
	private final Double noExamRate = 0.4;
	private final Double bloodTestRate = 0.35;
	private final Double xRayRate = 0.2;
	

	public Consultation(String name, ProbabilityDistribution durationProbability, Double cost, EmergencyDepartment emergencyDepartment) {
		super(name, durationProbability, cost, emergencyDepartment);
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public Task getNextTask() {
//		// TODO Auto-generated method stub
//		Patient severePatient = this.getNextSeverePatient();
//		Patient lightPatient = this.getNextLightPatient();
//		if (this.canTreatPatient(severePatient)) {
//			return new Task(this.emergencyDepartment.getTime(), new StartService(this, severePatient));				
//		}
//		else if (this.canTreatPatient(lightPatient)) {
//			return new Task(this.emergencyDepartment.getTime(), new StartService(this, lightPatient));				
//		}
//		else {
//			return this.tasksQueue.getNextTask();
//		}
//	}
	
	/**
	 * This method overrides canTreatPatient of WorkflowElement.
	 * It checks if there is an available nurse for registering the patient.
	 * @param patient
	 * @return boolean: true if the patient can be treated by the service, false otherwise
	 * @see WorkflowElement.canTreatPatient
	 */
	@Override
	public boolean canTreatPatient(Patient patient) {
		return (patient != null & patient.getPhysician() == null & emergencyDepartment.getIdlePhysician() != null) || (patient != null & patient.getPhysician() != null);
	}

	/**
	 * This method overrides startServiceOnPatient of WorkflowElement.
	 * It simulates the consultation of a patient by a physician.
	 * Either the patient is already overseen by a physician, or an available physician start overseeing the patient.
	 * At the end, it generates the endTask using generateEndTask from WorkflowElement.
	 * @see WorkflowElement.generateEndTask.
	 */
	@Override
	public void startServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		this.waitingQueue.remove(patient);
		Physician physician;
		if(patient.getPhysician() != null) {
			physician = patient.getPhysician();
		}
		else {
			physician = emergencyDepartment.getIdlePhysician();
			physician.addOverseenPatient(patient);
		}
		physician.setState("visiting");
		Event beginConsultation = new Event("Consultation beginning", emergencyDepartment.getTime());
		patient.addEvent(beginConsultation);
		patient.setState("being-visited");
		this.generateEndTask(this, patient, physician);
	}

	/**
	 * This method overrides endServiceOnPatient of WorkflowElement.
	 * It ends the installation of a patient before sending him to the waiting queue of consultation.
	 * First it updates the patient, the nurse, and the room information.
	 * Then it adds the patient to the consultation waiting queue.
	 * @param patient
	 */
	@Override
	public void endServiceOnPatient(Patient patient) {
		// TODO Auto-generated method stub
		this.examinePatient(patient);
		Event endConsultation = new Event("Consultation ending", emergencyDepartment.getTime());
		patient.addEvent(endConsultation);
		patient.addCharges(cost);
		patient.setState("waiting");
		patient.getLocation().removePatient(patient);
		patient.setLocation(null);
		this.examinePatient(patient);
	}

	private String determineExamination() {
		// TODO Auto-generated method stub
		Random random = new Random();
		Double exam = random.nextDouble();
		if(exam < noExamRate) {
			return "Release";
		}
		else if (exam < (noExamRate + bloodTestRate)) {
			return "BloodTest";
		}
		else if (exam < (noExamRate + bloodTestRate + xRayRate)) {
			return "XRay";
		}
		else {
			return "MRI";
		}
	}
	
	public void examinePatient(Patient patient) {
		String examination = this.determineExamination();
		if(examination != "Release") {
			patient.addEvent(new Event(examination + " prescribed", this.emergencyDepartment.getTime()));
			emergencyDepartment.getService("Transportation").addPatientToWaitingList(patient);
		}
		else {
			patient.addEvent(new Event("Released", this.emergencyDepartment.getTime()));
			patient.getPhysician().addTreatedPatient(patient);
			patient.getPhysician().removeOverseenPatient(patient);
			emergencyDepartment.removePatient(patient);
		}
	}

}
