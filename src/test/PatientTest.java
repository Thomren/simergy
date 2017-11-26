package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.PatientFactory;
import resources.Patient;

public class PatientTest {

	private EmergencyDepartment emergencyDepartment;
	private PatientFactory humanFactory;
	private Patient patient1;
	private Patient patient2;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		humanFactory = new PatientFactory();
		patient1 = humanFactory.create(emergencyDepartment);
		patient2 = humanFactory.create(emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		humanFactory = null;
		patient1 = null;
		patient2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(patient1.equals(patient1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(patient1.equals(patient2));
	}

}
