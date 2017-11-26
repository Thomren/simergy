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
import resources.BoxRoom;
import resources.Nurse;
import resources.Patient;
import resources.Room;
import resources.ShockRoom;
import resources.WaitingRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
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
		patient.setHealthInsurance(new NoInsurance());
		nurse = ED.getIdleNurse();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        installation = null;
        consultation = null;
        waitingRoom = null;
        patient = null;
        nurse = null;
    }
	
	@Test
	public void testStartServiceOnPatientLight() {
		// Initialization
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
		// Initialization
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
	public void testEndServiceOnPatientLight() {
		// Initialization
		patient.setSeverityLevel(ED.getSeverityLevel(3));
		// Simulate startServiceOnPatient effects
		patient.setState("being-transported");
		// Execution
		installation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Installation ending"));
		assertTrue(patient.getLocation().equals(shockRoom));
		assertTrue(patient.getCharges() == 3.);
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
		// Test on consultation service
		assertTrue(consultation.getWaitingQueue().size() == 1);
		// Test on installation service
		assertTrue(installation.getWaitingQueue().isEmpty());
	}
	
	@Test
	public void testEndServiceOnPatientSevere() {
		// Initialization
		patient.setSeverityLevel(ED.getSeverityLevel(2));
		// Simulate startServiceOnPatient effects
		patient.setState("being-transported");
		// Execution
		installation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Installation ending"));
		assertTrue(patient.getLocation().equals(boxRoom));
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
		// Test on consultation service
		assertTrue(consultation.getWaitingQueue().size() == 1);
		// Test on installation service
		assertTrue(installation.getWaitingQueue().isEmpty());
	}
	
	@Test
	public void testCanTreatPatientWhenTrue() {
		assertTrue(installation.canTreatPatient(patient));
	}
	
	@Test
	public void testCanTreatPatientWhenPatientIsNull() {
		assertFalse(installation.canTreatPatient(null));
	}
	
	@Test
	public void testCanTreatPatientWhenNoRoomAvailable() {
		boxRoom.addPatient(new Patient("John", "Doe", new NoInsurance(), ED.getSeverityLevel(3), ED));
		assertFalse(installation.canTreatPatient(patient));
	}
	
	@Test
	public void testCanTreatPatientWhenNoNurseAvailable() {
		nurse.setState("occupied");
		assertFalse(installation.canTreatPatient(patient));
	}
	
	@Test
	public void testGetNextTaskWhenLightPatientToBeTreated() {
		// Initialization
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
		// Initialization
		Task addedTask = new Task(5., new EndService(installation, patient));
		installation.getTasksQueue().addTask(addedTask);
		// Execution
		Task task = installation.getNextTask();
		// Test
		assertTrue(task.equals(addedTask));
	}
	
}
