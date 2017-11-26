package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.ShockRoom;

public class ShockRoomTest {
	private ShockRoom shockRoom1;
	private ShockRoom shockRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		shockRoom1 = new ShockRoom("room", 1, emergencyDepartment);
		shockRoom2 = new ShockRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		shockRoom1 = null;
		shockRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(shockRoom1.equals(shockRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(shockRoom1.equals(shockRoom2));
	}

}
