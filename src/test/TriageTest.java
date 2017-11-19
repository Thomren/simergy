package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.DeterministicDistribution;
import core.EmergencyDepartment;
import core.NurseFactory;
import core.ProbabilityDistribution;
import fr.ecp.is1220.tutorial7.ex2.BoardReader;
import fr.ecp.is1220.tutorial7.ex2.MessageBoard;
import fr.ecp.is1220.tutorial7.ex2.Observer;
import resource.BloodTestRoom;
import resource.BoxRoom;
import resource.MRIRoom;
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
	private WaitingRoom wr;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		ED.setServices(new WorkflowElement[]{new Triage("Triage", new DeterministicDistribution(1), ED, 0.)});
		wr = new WaitingRoom("Waiting Room 1", 10, ED);
		ED.addRoom(wr);
		triage = (Triage) ED.getService("Triage");
		ED.patientArrival(ED.getPatientFactory().create(ED.getSeverityLevel(3), ED));
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(1, nurseFactory);
		System.out.println("=== End of initialisation ===");
	}

	@Test
	public void testStartServiceOnPatient() {
		Patient patient = ED.getPatients().get(0);
		triage.startServiceOnPatient(patient);
		assertTrue(patient.getState().equals("being-registrated"));
		assertNull(patient.getLocation());
		assertTrue(wr.getPatients().size() == 0);
		fail("Not yet implemented");
	}

	@Test
	public void testEndServiceOnPatient() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPatientToWaitingList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextPatient() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextAvailableEmployeeTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleNextPatient() {
		fail("Not yet implemented");
	}

}
