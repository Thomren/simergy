package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;
import utils.DoorToDoctorTime;

public class DoorToDoctorTimeTest {
	
	private EmergencyDepartment ED;
	private DoorToDoctorTime dtdt;
	private Patient p1;

	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		ED = new EmergencyDepartment("Test ED");
		dtdt = new DoorToDoctorTime(ED);
		p1 = ED.getPatientFactory().create(ED.getSeverityLevel(3), ED);
		ED.patientArrival(p1);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
        ED = null;
        p1 = null;
    }

	@Test
	public void testComputeKPI() {
		p1.setArrivalTime(3);
		p1.addEvent(new Event("Installation beginning", 12));
		p1.addEvent(new Event("Consultation beginning", 15));
		p1.addEvent(new Event("Released", 25));
		assertTrue(dtdt.computeKPI(p1) == 12);
	}
	
	@Test
	public void testComputeKPIWhenNotReleased() {
		assertTrue(dtdt.computeKPI(p1) == Double.POSITIVE_INFINITY);
	}

}
