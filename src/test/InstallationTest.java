package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.DeterministicDistribution;
import core.EmergencyDepartment;
import core.NurseFactory;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resource.BoxRoom;
import resource.Nurse;
import resource.Patient;
import resource.Room;
import resource.ShockRoom;
import resource.WaitingRoom;
import workflow.Consultation;
import workflow.Installation;
import workflow.Triage;
import workflow.WorkflowElement;

public class InstallationTest {

	private EmergencyDepartment ED;
	private Installation installation;
	private Consultation consultation;
	private WaitingRoom waitingRoom;
	private BoxRoom boxRoom;
	private ShockRoom shockRoom;
	private Patient patient;
	private Nurse nurse;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), ED, 2.), 
				new Installation("Installation", new DeterministicDistribution(2), ED, 3.),
				new Consultation("Consultation", new DeterministicDistribution(4), 25., ED)});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		boxRoom = new BoxRoom("Box Room", 1, ED);
		shockRoom = new ShockRoom("Shock Room", 1, ED);
		ED.addRooms(new Room[]{waitingRoom, boxRoom, shockRoom});
		installation = (Installation) ED.getService("Installation");
		consultation = (Consultation) ED.getService("Consultation");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(1, nurseFactory);
		patient = ED.getPatients().get(0);
		nurse = ED.getIdleNurse();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        installation = null;
        waitingRoom = null;
        patient = null;
        nurse = null;
    }
	
	@Test
	public void testStartServiceOnPatientLight() {
		// Initialisation
		patient.setSeverityLevel(ED.getSeverityLevel(3));
		// Execution
		installation.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-installed"));
		assertNull(patient.getLocation());
		assertTrue(boxRoom.getPatients().contains(patient));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Installation beginning"));
		// Test on nurse
		assertTrue(nurse.getState().equals("occupied"));
	}
	
	@Test
	public void testStartServiceOnPatientSevere() {
		// Initialisation
		patient.setSeverityLevel(ED.getSeverityLevel(2));
		// Execution
		installation.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-installed"));
		assertNull(patient.getLocation());
		assertTrue(shockRoom.getPatients().contains(patient));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Installation beginning"));
		// Test on nurse
		assertTrue(nurse.getState().equals("occupied"));
	}

	@Test
	public void testEndServiceOnPatient() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-transported");
		// Execution
		installation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		System.out.println(patient.getCharges());
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Installation ending"));
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
		// Test on consultation service
		assertTrue(consultation.getWaitingQueue().size() == 1);
		// Test on installation service
		assertTrue(installation.getWaitingQueue().isEmpty());
	}
	
	@Test
	public void testGetNextTaskWhenLightPatientToBeTreated() {
		// Initialisation
		patient.setSeverityLevel(ED.getSeverityLevel(3));
		installation.addPatientToWaitingList(patient);
		ED.setTime(12.);
		// Execution
		Task task = installation.getNextTask();
		// Test
		assertTrue(task.getTimestamp() == 12.);
		assertTrue(task.getCommand() instanceof StartService);
		assertTrue(((StartService) task.getCommand()).getPatient().equals(patient));
		assertTrue(((StartService) task.getCommand()).getService().equals(installation));
	}
	
	@Test
	public void testGetNextTaskWhenSeverePatientToBeTreated() {
		// Initialisation
		patient.setSeverityLevel(ED.getSeverityLevel(2));
		installation.addPatientToWaitingList(patient);
		ED.setTime(12.);
		// Execution
		Task task = installation.getNextTask();
		// Test
		assertTrue(task.getTimestamp() == 12.);
		assertTrue(task.getCommand() instanceof StartService);
		assertTrue(((StartService) task.getCommand()).getPatient().equals(patient));
		assertTrue(((StartService) task.getCommand()).getService().equals(installation));
	}
	
	@Test
	public void testGetNextTaskWhenNothingToDo() {
		// Execution
		Task task = installation.getNextTask();
		// Test
		assertNull(task);
	}
	
	@Test
	public void testGetNextTaskWhenEndServiceOnPatient() {
		// Initialisation
		Task addedTask = new Task(5., new EndService(installation, patient));
		installation.getTasksQueue().addTask(addedTask);
		// Execution
		Task task = installation.getNextTask();
		// Test
		assertTrue(task.equals(addedTask));
	}
	
}
