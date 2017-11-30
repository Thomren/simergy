package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resources.Patient;
import resources.Room;
import resources.WaitingRoom;
import resources.XRayRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import workflow.Installation;
import workflow.Triage;
import workflow.WorkflowElement;
import workflow.XRay;

public class HealthServiceTest {
	
	private EmergencyDepartment ED;
	private Installation installation;
	private XRay xRay;
	private XRayRoom xRayRoom;
	private Patient patient;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage(new DeterministicDistribution(1), 2., ED), 
				new Installation (new DeterministicDistribution(2), 3., ED),
				new XRay(new DeterministicDistribution(4), 12., ED)
				});
		xRayRoom = new XRayRoom("XRay Room", 1, ED);
		WaitingRoom waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		ED.addRooms(new Room[]{xRayRoom, waitingRoom});
		installation = (Installation) ED.getService("Installation");
		xRay = (XRay) ED.getService("XRay");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		patient = ED.getPatients().get(0);
		patient.setHealthInsurance(new NoInsurance());
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        installation = null;
        xRay = null;
        xRayRoom = null;
        patient = null;
    }
	
	@Test
	public void testStartServiceOnPatient() {
		// Execution
		xRay.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("taking-XRay"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("XRay beginning"));
	}

	@Test
	public void testEndServiceOnPatient() {
		// Simulate startServiceOnPatient effects
		patient.setState("taking-XRay");
		// Execution
		xRay.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getCharges() == 12.);
		// Test on XRay service
		assertTrue(xRay.getWaitingQueue().isEmpty());
		// Test on destination service
		assertTrue(installation.getWaitingQueue().size() == 1);
	}
	
	@Test
	public void testGetNextTaskWhenPatientToBeTreated() {
		// Initialization
		xRay.addPatientToWaitingList(patient);
		ED.setTime(11.);
		// Execution
		Task task = xRay.getNextTask();
		// Test
		assertTrue(task.getTimestamp() == 11.);
		assertTrue(task.getCommand() instanceof StartService);
		assertTrue(((StartService) task.getCommand()).getPatient().equals(patient));
		assertTrue(((StartService) task.getCommand()).getService().equals(xRay));
	}
	
	@Test
	public void testGetNextTaskWhenNothingToDo() {
		// Execution
		Task task = xRay.getNextTask();
		// Test
		assertNull(task);
	}
	
	@Test
	public void testGetNextTaskWhenEndServiceOnPatient() {
		// Initialization
		Task addedTask = new Task(5., new EndService(xRay, patient));
		xRay.getTasksQueue().addTask(addedTask);
		// Execution
		Task task = xRay.getNextTask();
		// Test
		assertTrue(task.equals(addedTask));
	}

}

