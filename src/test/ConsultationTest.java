package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.PhysicianFactory;
import processing.EndService;
import processing.StartService;
import processing.Task;
import resources.BoxRoom;
import resources.Patient;
import resources.Physician;
import resources.Room;
import resources.ShockRoom;
import resources.WaitingRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import workflow.Consultation;
import workflow.Transportation;
import workflow.Triage;
import workflow.WorkflowElement;

public class ConsultationTest {

	private EmergencyDepartment ED;
	private Transportation transportation;
	private Consultation consultation;
	private WaitingRoom waitingRoom;
	private BoxRoom boxRoom;
	private ShockRoom shockRoom;
	private Patient patient;
	private Physician physician;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage(new DeterministicDistribution(1), 2., ED), 
				new Transportation (new DeterministicDistribution(2), 3., ED),
				new Consultation(new DeterministicDistribution(4), 25., ED)
				});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		boxRoom = new BoxRoom("Box Room", 1, ED);
		shockRoom = new ShockRoom("Shock Room", 1, ED);
		ED.addRooms(new Room[]{waitingRoom, boxRoom, shockRoom});
		transportation = (Transportation) ED.getService("Transportation");
		consultation = (Consultation) ED.getService("Consultation");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		PhysicianFactory physicianFactory = new PhysicianFactory();
		ED.addStaff(1, physicianFactory);
		patient = ED.getPatients().get(0);
		patient.setHealthInsurance(new NoInsurance());
		physician = ED.getIdlePhysician();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        transportation = null;
        consultation = null;
        waitingRoom = null;
        patient = null;
        physician = null;
    }
	
	@Test
	public void testStartServiceOnPatient() {
		// Execution
		consultation.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-visited"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Consultation beginning"));
		assertTrue(patient.getPhysician().equals(physician));
		// Test on nurse
		assertTrue(physician.getState().equals("visiting"));
	}

	@Test
	public void testEndServiceOnPatientWhenBloodTest() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-visited");
		patient.addObserver(physician);
		consultation.setNoExamRate(0.);
		consultation.setBloodTestRate(1.);
		consultation.setXRayRate(0.);
		// Execution
		consultation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 2).getName().equals("Consultation ending"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("BloodTest prescribed"));
		assertTrue(patient.getCharges() == 25.);
		// Test on nurse
		assertTrue(physician.getState().equals("idle"));
		// Test on consultation service
		assertTrue(consultation.getWaitingQueue().isEmpty());
		// Test on destination service
		assertTrue(transportation.getWaitingQueue().size() == 1);
	}
	
	@Test
	public void testEndServiceOnPatientWhenNothing() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-visited");
		patient.setLocation(boxRoom);
		boxRoom.addPatient(patient);
		patient.addObserver(physician);
		consultation.setNoExamRate(1.);
		consultation.setBloodTestRate(0.);
		consultation.setXRayRate(0.);
		// Execution
		consultation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("released"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 2).getName().equals("Consultation ending"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Released"));
		assertNull(patient.getLocation());
		assertTrue(patient.getCharges() == 25.);
		// Test on physician
		assertTrue(physician.getState().equals("idle"));
		assertTrue(physician.getTreatedPatients().get(0).equals(patient));
		assertTrue(physician.getOverseenPatients().size() == 0);
		// Test on room
		assertTrue(boxRoom.getPatients().size() == 0);
		// Test on consultation service
		assertTrue(consultation.getWaitingQueue().isEmpty());
	}
	
	@Test
	public void testGetNextTaskWhenPatientToBeTreated() {
		// Initialization
		consultation.addPatientToWaitingList(patient);
		ED.setTime(11.);
		// Execution
		Task task = consultation.getNextTask();
		// Test
		assertTrue(task.getTimestamp() == 11.);
		assertTrue(task.getCommand() instanceof StartService);
		assertTrue(((StartService) task.getCommand()).getPatient().equals(patient));
		assertTrue(((StartService) task.getCommand()).getService().equals(consultation));
	}
	
	@Test
	public void testGetNextTaskWhenNothingToDo() {
		// Execution
		Task task = consultation.getNextTask();
		// Test
		assertNull(task);
	}
	
	@Test
	public void testGetNextTaskWhenEndServiceOnPatient() {
		// Initialization
		Task addedTask = new Task(5., new EndService(consultation, patient));
		consultation.getTasksQueue().addTask(addedTask);
		// Execution
		Task task = consultation.getNextTask();
		// Test
		assertTrue(task.equals(addedTask));
	}

}
