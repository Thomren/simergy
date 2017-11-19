package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.DeterministicDistribution;
import core.EmergencyDepartment;
import core.NurseFactory;
import core.ProbabilityDistribution;
import resource.BloodTestRoom;
import resource.BoxRoom;
import resource.MRIRoom;
import resource.Nurse;
import resource.Patient;
import resource.Room;
import resource.ShockRoom;
import resource.WaitingRoom;
import resource.XRayRoom;
import workflow.Triage;
import workflow.WorkflowElement;

public class TriageTest {
	
	private EmergencyDepartment ED;
	private Triage triage;
	private WaitingRoom waitingRoom;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), ED, 2.)});
		waitingRoom = new WaitingRoom("Waiting Room", 10, ED);
		ED.addRoom(waitingRoom);
		triage = (Triage) ED.getService("Triage");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(1, nurseFactory);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        triage = null;
        waitingRoom = null;
    }

	@Test
	public void testStartServiceOnPatient() {
		// Inspected objects
		Patient patient = ED.getPatients().get(0);
		Nurse nurse = ED.getIdleNurse();
		// Execution
		triage.startServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("being-registrated"));
		assertNull(patient.getLocation());
		assertTrue(waitingRoom.getPatients().size() == 0);
		assertTrue(patient.getHistory().size() == 1);
		// Test on nurse
		assertTrue(nurse.getState().equals("occupied"));
	}

	@Test
	public void testEndServiceOnPatient() {
		// Inspected objects
		Patient patient = ED.getPatients().get(0);
		Nurse nurse = (Nurse) ED.getStaff().get(0);
		// Simulate startServiceOnPatient effects
		patient.setState("being-registrated");
		patient.setLocation(null);
		waitingRoom.removePatient(patient);
		// Execution
		triage.endServiceOnPatient(patient);
		// Tests on patient
		assertTrue(patient.getState().equals("waiting"));
		assertTrue(patient.getCharges() == 2.);
		assertTrue(patient.getLocation().getName() == "Waiting Room");
		assertTrue(waitingRoom.getPatients().contains(patient));
		// Test on nurse
		assertTrue(nurse.getState().equals("idle"));
	}

	@Test
	public void testAddPatientToWaitingList() {
		// Inspected object
		Patient patient = ED.getPatients().get(0);
		// Execution
		triage.addPatientToWaitingList(patient);
		// Test
		assertTrue(triage.getWaitingQueue().contains(patient));
	}

	@Test
	public void testGetNextPatient() {
		// Inspected object
		Patient patient = ED.getPatients().get(0);
		// Initialisation
		triage.getWaitingQueue().add(patient);
		// Test
		assertTrue(triage.getNextPatient().equals(patient));	
	}

}
