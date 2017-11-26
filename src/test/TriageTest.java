package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.NurseFactory;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resources.BloodTestRoom;
import resources.BoxRoom;
import resources.MRIRoom;
import resources.Nurse;
import resources.Patient;
import resources.Room;
import resources.ShockRoom;
import resources.WaitingRoom;
import resources.XRayRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import utils.ProbabilityDistribution;
import workflow.Transportation;
import workflow.Triage;
import workflow.WorkflowElement;

public class TriageTest {
	
	private EmergencyDepartment ED;
	private Triage triage;
	private Transportation transportation;
	private WaitingRoom waitingRoom;
	private Patient patient;
	private Nurse nurse;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), ED, 2.),
				new Transportation("Transportation", new DeterministicDistribution(2), ED, 3.)});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		ED.addRoom(waitingRoom);
		triage = (Triage) ED.getService("Triage");
		transportation = (Transportation) ED.getService("Transportation");
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
        transportation = null;
        waitingRoom = null;
        patient = null;
        nurse = null;
    }

	@Test
	public void testStartServiceOnPatient() {
		// Execution
		triage.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-registrated"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Registration beginning"));
		// Test on nurse
		assertTrue(nurse.getState().equals("occupied"));
	}

	@Test
	public void testEndServiceOnPatient() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-registrated");
		patient.setLocation(null);
		waitingRoom.removePatient(patient);
		// Execution
		triage.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getCharges() == 2.);
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Registration ending"));
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
		// Test on transportation service
		assertTrue(transportation.getWaitingQueue().size() == 1);
		// Test on triage service
		System.out.println("*****" + triage.getWaitingQueue().equals(patient));
		assertTrue(triage.getWaitingQueue().isEmpty());
	}

	@Test
	public void testAddPatientToWaitingList() {
		// Initialisation
		triage.setWaitingQueue(new ArrayList<Patient>());
		// Execution
		triage.addPatientToWaitingList(patient);
		// Test
		assertTrue(triage.getWaitingQueue().contains(patient));
	}
	
	@Test
	public void testRemovePatientFromWaitingList() {
		// Execution
		triage.removePatientFromWaitingList(patient);
		// Test
		assertTrue(triage.getWaitingQueue().isEmpty());
	}

	@Test
	public void testGetNextPatient() {
		// Initialisation
		triage.getWaitingQueue().add(patient);
		// Test
		assertTrue(triage.getNextPatient().equals(patient));	
	}
	
	@Test
	public void testGetNextTaskWhenNothingToDo() {
		// Initialisation
		triage.removePatientFromWaitingList(patient);
		// Execution
		Task task = triage.getNextTask();
		// Test
		assertNull(task);
	}
	
	@Test
	public void testGetNextTaskWhenStartService() {
		// Initialisation
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
		// Initialisation
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
		fail("Not yet implemented");
	}

}
