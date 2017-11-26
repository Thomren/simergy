package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.TransporterFactory;
import resources.Transporter;

public class TransporterTest {
	private EmergencyDepartment emergencyDepartment;
	private TransporterFactory humanFactory;
	private Transporter transporter1;
	private Transporter transporter2;

	@Before
	public void setUp() throws Exception {
		emergencyDepartment = new EmergencyDepartment("ED");
		humanFactory = new TransporterFactory();
		transporter1 = humanFactory.create(emergencyDepartment);
		transporter2 = humanFactory.create(emergencyDepartment);
	}

	@After
	public void tearDown() throws Exception {
		emergencyDepartment = null;
		humanFactory = null;
		transporter1 = null;
		transporter2 = null;
	}

	@Test
	public void equalsTest() {
		assertTrue(transporter1.equals(transporter1));
	}
	
	@Test
	public void notEqualsTest() {
		assertFalse(transporter1.equals(transporter2));
	}
}
