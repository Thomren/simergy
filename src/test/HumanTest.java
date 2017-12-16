package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Event;
import core.NurseFactory;
import resources.Human;

public class HumanTest {
	private EmergencyDepartment emergencyDepartment;
	private NurseFactory humanFactory;
	private Human human1;
	private Event event;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		humanFactory = new NurseFactory();
		human1 = humanFactory.create(emergencyDepartment);
		humanFactory.create(emergencyDepartment);
		event = new Event("test event", 2.0);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		humanFactory = null;
		human1 = null;
		event = null;
	}

	@Test
	public void addEventTest() {
		human1.addEvent(event);
		assertEquals(event, human1.getHistory().get(0));
	}
	
	@Test
	public void addGetHistoryTime() {
		human1.addEvent(event);
		assertTrue(2.0 == human1.getHistoryTime());
	}
}
