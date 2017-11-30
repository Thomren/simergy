package test;

import core.EmergencyDepartment;
import core.NurseFactory;
import core.PhysicianFactory;
import core.TransporterFactory;
import processing.Task;
import resources.BloodTestRoom;
import resources.BoxRoom;
import resources.MRIRoom;
import resources.Patient;
import resources.ShockRoom;
import resources.WaitingRoom;
import resources.XRayRoom;
import utils.DeterministicDistribution;
import utils.ExponentialDistribution;
import utils.SeverityLevel;
import utils.SeverityLevel_L1;
import utils.SeverityLevel_L2;
import utils.SeverityLevel_L3;
import utils.SeverityLevel_L4;
import utils.SeverityLevel_L5;
import workflow.BloodTest;
import workflow.Consultation;
import workflow.Installation;
import workflow.MRI;
import workflow.Transportation;
import workflow.Triage;
import workflow.WorkflowElement;
import workflow.XRay;

/**
 * This class is a manual user case in order to test the first version of the app
 * @author Thomas
 *
 */
public class ManualUseCase {
	public static void executeService(String name, Patient patient, EmergencyDepartment ED){
		WorkflowElement workflowElement = ED.getService(name);
		workflowElement.startServiceOnPatient(patient);
		workflowElement.generateEndTask(workflowElement, patient);
		Task endService = workflowElement.getTasksQueue().getNextTask();
		workflowElement.getTasksQueue().removeTask(endService);
		endService.getCommand().execute();
		ED.setTime(endService.getTimestamp());
	}
	
	public static void main(String[] args) {
		System.out.println("Beginning of manual use case");
		// Create the ED
		EmergencyDepartment ED = new EmergencyDepartment("CentruleRupelec");
		// Add 5 waiting rooms with a capacity of 10
		WaitingRoom wr1 = new WaitingRoom("Waiting Room 1", 10, ED);
		ED.addRoom(wr1);
		WaitingRoom wr2 = new WaitingRoom("Waiting Room 2", 10, ED);
		ED.addRoom(wr2);
		WaitingRoom wr3 = new WaitingRoom("Waiting Room 3", 10, ED);
		ED.addRoom(wr3);
		WaitingRoom wr4 = new WaitingRoom("Waiting Room 4", 10, ED);
		ED.addRoom(wr4);
		WaitingRoom wr5 = new WaitingRoom("Waiting Room 5", 10, ED);
		ED.addRoom(wr5);
		// Add other rooms
		BoxRoom r1 = new BoxRoom("Box Room 1", 1, ED);
		ED.addRoom(r1);
		ShockRoom r2 = new ShockRoom("Shock Room 1", 1, ED);
		ED.addRoom(r2);
		XRayRoom r3 = new XRayRoom("XRay Room 1", 1, ED);
		ED.addRoom(r3);
		MRIRoom r4 = new MRIRoom("MRI Room 1", 1, ED);
		ED.addRoom(r4);
		BloodTestRoom r5 = new BloodTestRoom("Blood Test Room 1", 1, ED);
		ED.addRoom(r5);
		// Add 4 physicians
		PhysicianFactory physicianFactory = new PhysicianFactory();
		ED.addStaff(4, physicianFactory);
		// Add 4 nurses
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(4, nurseFactory);
		// Add 4 transporters
		TransporterFactory transporterFactory = new TransporterFactory();
		ED.addStaff(4, transporterFactory);
		// Set Severity levels distributions
		ED.setSeverityLevels(new SeverityLevel[] {
				new SeverityLevel_L1(new ExponentialDistribution(0.001)),
				new SeverityLevel_L2(new ExponentialDistribution(0.005)),
				new SeverityLevel_L3(new ExponentialDistribution(0.01)),
				new SeverityLevel_L4(new ExponentialDistribution(0.02)),
				new SeverityLevel_L5(new ExponentialDistribution(0.05))
		});
		// Set Services
		ED.setServices(new WorkflowElement[] {
				new BloodTest(new DeterministicDistribution(5), 15., ED),
				new Consultation(new DeterministicDistribution(2), 5., ED),
				new Installation(new DeterministicDistribution(5), 0., ED),
				new MRI(new DeterministicDistribution(10), 20., ED),
				new Transportation( new DeterministicDistribution(5), 0., ED),
				new Triage(new DeterministicDistribution(1), 0., ED),
				new XRay(new DeterministicDistribution(15), 50., ED)});
		// Arrival of a patient
		Task patientArrivalTask = ED.getNextPatientArrival();
		patientArrivalTask.getCommand().execute();
		ED.setTime(patientArrivalTask.getTimestamp());
		Patient patient = ED.getPatients().get(0);
		// Triage
		executeService("Triage", patient, ED);
		// Installation
		executeService("Installation", patient, ED);
		// Consultation
		executeService("Consultation", patient, ED);
		// Released or exam
		while(patient.getHistory().get(patient.getHistory().size() - 1).getName() != "Released") {
			switch (patient.getHistory().get(patient.getHistory().size() - 1).getName()) {
				case "BloodTest prescribed":
					// Transportation
					executeService("Transportation", patient, ED);
					executeService("BloodTest", patient, ED);
					break;
				case "XRay prescribed":
					// Transportation
					executeService("Transportation", patient, ED);
					executeService("XRay", patient, ED);
					break;
				case "MRI prescribed":
					// Transportation
					executeService("Transportation", patient, ED);
					executeService("MRI", patient, ED);
					break;
			}
			executeService("Installation", patient, ED);
			executeService("Consultation", patient, ED);
		}
		patient.printReport();
		ED.printReport();
		System.out.println("Patient successfully treated and left hospital !");
	}
}
