package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Event;
import core.TransporterFactory;
import resources.BloodTestRoom;
import resources.Patient;
import resources.Room;
import resources.Transporter;
import resources.WaitingRoom;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import workflow.BloodTest;
import workflow.Transportation;
import workflow.Triage;
import workflow.WorkflowElement;

public class TransportationTest {

	private EmergencyDepartment ED;
	private Transportation transportation;
	private BloodTest bloodTest;
	private WaitingRoom waitingRoom;
	private BloodTestRoom bloodTestRoom;
	private Patient patient;
	private Transporter transporter;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), 2., ED), 
				new Transportation("Transportation", new DeterministicDistribution(2), 3., ED),
				new BloodTest("BloodTest", new DeterministicDistribution(4), 25., ED)});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		bloodTestRoom = new BloodTestRoom("Blood Test Room 1", 1, ED);
		ED.addRooms(new Room[]{waitingRoom, bloodTestRoom});
		transportation = (Transportation) ED.getService("Transportation");
		bloodTest = (BloodTest) ED.getService("BloodTest");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		TransporterFactory transporterFactory = new TransporterFactory();
		ED.addStaff(1, transporterFactory);
		patient = ED.getPatients().get(0);
		patient.setHealthInsurance(new NoInsurance());
		transporter = ED.getIdleTransporter();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        transportation = null;
        bloodTest = null;
        waitingRoom = null;
        patient = null;
        transporter = null;
    }
	
	@Test
	public void testStartServiceOnPatient() {
		// Initialization
		patient.setLocation(waitingRoom);
		waitingRoom.addPatient(patient);
		patient.addEvent(new Event("BloodTest prescribed", 0.));
		// Execution
		transportation.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-transported"));
		assertTrue(bloodTestRoom.getPatients().contains(patient));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Transportation beginning"));
		// Test on nurse
		assertTrue(transporter.getState().equals("occupied"));
	}

	@Test
	public void testEndServiceOnPatient() {
		// Simulate startServiceOnPatient effects
		patient.setState("being-transported");
		patient.setLocation(bloodTestRoom);
		// Execution
		transportation.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getHistory().get(patient.getHistory().size() - 1).getName().equals("Transportation ending"));
		assertTrue(patient.getLocation().equals(bloodTestRoom));
		assertTrue(patient.getCharges() == 3.);
		// Test on nurse
		assertTrue(transporter.getState().equals("idle"));
		// Test on consultation service
		assertTrue(bloodTest.getWaitingQueue().size() == 1);
		// Test on installation service
		assertTrue(transportation.getWaitingQueue().isEmpty());
	}
	
	@Test
	public void testCanTreatPatientWhenTrue() {
		patient.addEvent(new Event("BloodTest prescribed", 0.));
		assertTrue(transportation.canTreatPatient(patient));
	}
	
	@Test
	public void testCanTreatPatientWhenPatientIsNull() {
		assertFalse(transportation.canTreatPatient(null));
	}
	
	@Test
	public void testCanTreatPatientWhenNoRoomAvailable() {
		bloodTestRoom.addPatient(new Patient("John", "Doe", new NoInsurance(), ED.getSeverityLevel(3), ED));
		assertFalse(transportation.canTreatPatient(patient));
	}
	
	@Test
	public void testCanTreatPatientWhenNoTransporterAvailable() {
		transporter.setState("occupied");
		assertFalse(transportation.canTreatPatient(patient));
	}
	
}

