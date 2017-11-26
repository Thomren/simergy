package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.NurseFactory;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resources.Nurse;
import resources.Patient;
import resources.WaitingRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import workflow.Installation;
import workflow.Triage;
import workflow.WorkflowElement;

public class TriageTest {
	
	private EmergencyDepartment ED;
	private Triage triage;
	private Installation installation;
	private WaitingRoom waitingRoom;
	private Patient patient;
	private Nurse nurse;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), ED, 2.),
				new Installation("Installation", new DeterministicDistribution(2), ED, 3.)});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		ED.addRoom(waitingRoom);
		triage = (Triage) ED.getService("Triage");
		installation = (Installation) ED.getService("Installation");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(1, nurseFactory);
		patient = ED.getPatients().get(0);
		patient.setHealthInsurance(new NoInsurance());
		nurse = ED.getIdleNurse();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        triage = null;
        installation = null;
        waitingRoom = null;
        patient = null;
        nurse = null;
    }

	@Test
	public void testStartServiceOnPatient() {
		// Execution
		triage.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-registered"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Registration beginning"));
		// Test on nurse
		assertTrue(nurse.getState().equals("occupied"));
		// Test on triage service
		assertTrue(triage.getWaitingQueue().isEmpty());
	}

	@Test
	public void testEndServiceOnPatient() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-registrated");
		triage.removePatientFromWaitingList(patient);
		// Execution
		triage.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getCharges() == 2.);
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Registration ending"));
		assertNull(patient.getLocation());
		assertTrue(waitingRoom.getPatients().size() == 0);
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
		// Test on installation service
		assertTrue(installation.getWaitingQueue().size() == 1);
	}
	
	@Test
	public void testCanTreatPatientWhenTrue() {
		assertTrue(triage.canTreatPatient(patient));
	}
	
	@Test 
	public void testCanTreatPatientWhenPatientIsNull() {
		assertFalse(triage.canTreatPatient(null));
	}
	
	@Test 
	public void testCanTreatPatientWhenNoAvailableNurse() {
		nurse.setState("occupied");
		assertFalse(triage.canTreatPatient(patient));
	}
	
	// Inherited methods
	
	@Test
	public void testGetNextTaskWhenStartService() {
		// Initialization
		ED.setTime(15.);
		// Execution
		Task task = triage.getNextTask();
		// Test
		assertTrue(task.getTimestamp() == 15.);
		assertTrue(task.getCommand() instanceof StartService);
		assertTrue(((StartService) task.getCommand()).getPatient().equals(patient));
		assertTrue(((StartService) task.getCommand()).getService().equals(triage));
	}
	
	@Test
	public void testGetNextTaskWhenEndService() {
		// Initialization
		triage.removePatientFromWaitingList(patient);
		Task addedTask = new Task(5., new EndService(triage, patient));
		triage.getTasksQueue().addTask(addedTask);
		// Execution
		Task task = triage.getNextTask();
		// Test
		assertTrue(task.equals(addedTask));
	}
	
	@Test
	public void testGetNextTaskWhenNoNurseAvailable() {
		// Initialization
		ED.setTime(15.);
		nurse.setState("occupied");
		// Execution
		Task task = triage.getNextTask();
		// Test
		assertNull(task);
	}

}
