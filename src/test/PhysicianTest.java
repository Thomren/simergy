package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.PhysicianFactory;
import resources.Physician;

public class PhysicianTest {

	private EmergencyDepartment emergencyDepartment;
	private PhysicianFactory humanFactory;
	private Physician physician1;
	private Physician physician2;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		humanFactory = new PhysicianFactory();
		physician1 = humanFactory.create(emergencyDepartment);
		physician2 = humanFactory.create(emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		humanFactory = null;
		physician1 = null;
		physician2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(physician1.equals(physician1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(physician1.equals(physician2));
	}

}
