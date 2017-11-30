package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import processing.EndService;
import processing.Task;
import resources.Patient;
import utils.DeterministicDistribution;
import utils.NoInsurance;
import utils.SeverityLevel_L1;
import utils.SeverityLevel_L3;
import workflow.Triage;

public class WorkflowElementTest {
	
	private EmergencyDepartment ED;
	private Triage triage;
	private Patient patient;
	private Patient severePatient;
	
	@Before
	public void setUp() throws Exception {
		ED = new EmergencyDepartment("Test Hospital");
		triage = new Triage(new DeterministicDistribution(5), 3., ED);
		patient = new Patient("John", "Doe", new NoInsurance(), new SeverityLevel_L3(new DeterministicDistribution(2)), ED);
		severePatient = new Patient("Henri", "Golo", new NoInsurance(), new SeverityLevel_L1(new DeterministicDistribution(2)), ED);
	}
	
   @After
    public void tearDown() {
        ED = null;
        triage = null;
        patient = null;
    }

	@Test
	public void testAddPatientToWaitingList() {
		triage.addPatientToWaitingList(patient);
		assertTrue(triage.getWaitingQueue().contains(patient));
	}

	@Test
	public void testRemovePatientFromWaitingList() {
		triage.addPatientToWaitingList(patient);
		triage.removePatientFromWaitingList(patient);
		assertTrue(triage.getWaitingQueue().isEmpty());
	}

	@Test
	public void testGetNextSeverePatient() {
		triage.addPatientToWaitingList(patient);
		triage.addPatientToWaitingList(severePatient);
		assertTrue(triage.getNextSeverePatient().equals(severePatient));
	}
	
	@Test
	public void testGetNextSeverePatientWhenNone() {
		triage.addPatientToWaitingList(patient);
		assertNull(triage.getNextSeverePatient());
	}

	@Test
	public void testGetNextLightPatient() {
		triage.addPatientToWaitingList(patient);
		triage.addPatientToWaitingList(severePatient);
		assertTrue(triage.getNextLightPatient().equals(patient));
	}

	@Test
	public void testGetNextLightPatientWhenNone() {
		triage.addPatientToWaitingList(severePatient);
		assertNull(triage.getNextLightPatient());
	}
	
	@Test
	public void testGetNextTaskWhenNothingToDo() {
		Task task = triage.getNextTask();
		assertNull(task);
	}

	@Test
	public void testGenerateEndTask() {
		triage.generateEndTask(triage, patient);
		assertTrue(triage.getTasksQueue().getQueue().peek().getTimestamp() == triage.getEmergencyDepartment().getTime() + 5);
		assertTrue(triage.getTasksQueue().getQueue().peek().getCommand() instanceof EndService);
	}

	@Test
	public void testRemoveNextTask() {
		triage.generateEndTask(triage, patient);
		triage.removeNextTask();
		assertTrue(triage.getTasksQueue().getQueue().size() == 0);
	}

}
