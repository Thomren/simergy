package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Event;
import core.NurseFactory;
import resources.Nurse;

public class NurseTest {
	private EmergencyDepartment emergencyDepartment;
	private NurseFactory humanFactory;
	private Nurse nurse1;
	private Nurse nurse2;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		humanFactory = new NurseFactory();
		nurse1 = humanFactory.create(emergencyDepartment);
		nurse2 = humanFactory.create(emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		humanFactory = null;
		nurse1 = null;
		nurse2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(nurse1.equals(nurse1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(nurse1.equals(nurse2));
	}
}
