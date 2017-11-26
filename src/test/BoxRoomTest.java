package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.BoxRoom;

public class BoxRoomTest {
	private BoxRoom boxRoom1;
	private BoxRoom boxRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		boxRoom1 = new BoxRoom("room", 1, emergencyDepartment);
		boxRoom2 = new BoxRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		boxRoom1 = null;
		boxRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(boxRoom1.equals(boxRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(boxRoom1.equals(boxRoom2));
	}
}
