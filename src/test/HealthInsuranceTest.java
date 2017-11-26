package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.GoldInsurance;
import utils.HealthInsurance;


/*
 * This is the JUnit test for HealthInsurance
 * @author Quentin
 */
public class HealthInsuranceTest {
	protected double price;
	protected HealthInsurance healthInsurance;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		price = 2.0;
		healthInsurance = new GoldInsurance();
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
	   healthInsurance = null;
    }
	
	@Test
	public void testGenerateSample() {
		System.out.println(healthInsurance.computeDiscountedPrice(10.0));
		assertTrue(healthInsurance.computeDiscountedPrice(10.0) == price);
	}
	
	@Test
	public void testNotGenerateSample() {
		System.out.println(healthInsurance.computeDiscountedPrice(10.1));
		assertFalse(healthInsurance.computeDiscountedPrice(10.1) == price);
	}

}
