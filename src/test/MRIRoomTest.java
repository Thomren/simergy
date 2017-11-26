package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import resources.MRIRoom;

public class MRIRoomTest {
	private MRIRoom MRIRoom1;
	private MRIRoom MRIRoom2;
	private EmergencyDepartment emergencyDepartment;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		MRIRoom1 = new MRIRoom("room", 1, emergencyDepartment);
		MRIRoom2 = new MRIRoom("room", 1, emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		MRIRoom1 = null;
		MRIRoom2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(MRIRoom1.equals(MRIRoom1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(MRIRoom1.equals(MRIRoom2));
	}

}
