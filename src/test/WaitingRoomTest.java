package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.WaitingRoom;

public class WaitingRoomTest {

	private WaitingRoom waitingRoom1;
	private WaitingRoom waitingRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		waitingRoom1 = new WaitingRoom("room", 1, emergencyDepartment);
		waitingRoom2 = new WaitingRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		waitingRoom1 = null;
		waitingRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(waitingRoom1.equals(waitingRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(waitingRoom1.equals(waitingRoom2));
	}

}
