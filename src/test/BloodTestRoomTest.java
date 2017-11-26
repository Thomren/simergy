package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.BloodTestRoom;

public class BloodTestRoomTest {
	private BloodTestRoom bloodTestRoom1;
	private BloodTestRoom bloodTestRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		bloodTestRoom1 = new BloodTestRoom("room", 1, emergencyDepartment);
		bloodTestRoom2 = new BloodTestRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		bloodTestRoom1 = null;
		bloodTestRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(bloodTestRoom1.equals(bloodTestRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(bloodTestRoom1.equals(bloodTestRoom2));
	}

}
