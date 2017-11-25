package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.ExponentialDistribution;

/*
 * This is the JUnit test for ExponentialDistribution
 * @author Quentin
 */
public class ExponentialDistributionTest {
	private Double lambda;
	private ExponentialDistribution exponentialDistribution;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		lambda = 0.5;
		exponentialDistribution = new ExponentialDistribution(lambda);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
	   exponentialDistribution = null;
    }
	
	@Test
	public void testGenerateSample() {
		int nSample = 100000;
		double sumSample = 0;
		for (int i = 0; i < nSample; i++) {
			sumSample = sumSample + exponentialDistribution.generateSample();
		}
		double average = sumSample/nSample;
		System.out.println(average);
		assertTrue((1/lambda)*0.95 < average & (1/lambda)*1.05 > average);
	}
}
