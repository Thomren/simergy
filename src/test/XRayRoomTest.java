package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.XRayRoom;

public class XRayRoomTest {
	private XRayRoom XRayRoom1;
	private XRayRoom XRayRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		XRayRoom1 = new XRayRoom("room", 1, emergencyDepartment);
		XRayRoom2 = new XRayRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		XRayRoom1 = null;
		XRayRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(XRayRoom1.equals(XRayRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(XRayRoom1.equals(XRayRoom2));
	}

}
