package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;
import utils.LengthOfStay;

public class LengthOfStayTest {
	
	private EmergencyDepartment ED;
	private LengthOfStay los;
	private Patient p1;
	private Patient p2;
	private Patient p3;
	private Patient p4;
	private Patient p5;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		los = new LengthOfStay(ED);
		p1 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		ED.patientArrival(p1);
		p2 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		p3 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		p4 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		p5 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		ED.patientArrival(p2);
		ED.patientArrival(p3);
		ED.patientArrival(p4);
		ED.patientArrival(p5);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        p1 = null;
        p2 = null;
        p3 = null;
        p4 = null;
        p5 = null;
    }

	@Test
	public void testComputeKPI() {
		p1.setArrivalTime(3);
		p1.addEvent(new Event("Released", 25));
		assertTrue(los.computeKPI(p1) == 22);
	}
	
	@Test
	public void testComputeKPIWhenNotReleased() {
		assertTrue(los.computeKPI(p1) == Double.POSITIVE_INFINITY);
	}

	@Test
	public void testComputeAverageKPI() {
		p1.setArrivalTime(3);
		p1.addEvent(new Event("Released", 25));
		p2.setArrivalTime(5);
		p2.addEvent(new Event("Released", 20));
		p3.setArrivalTime(10);
		p3.addEvent(new Event("Released", 15));
		p4.setArrivalTime(11);
		p4.addEvent(new Event("Released", 31));
		p5.setArrivalTime(25);
		p5.addEvent(new Event("Released", 50));
		assertTrue(los.computeAverageKPI() == 17.4);
	}

}
