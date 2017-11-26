package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.UniformDistribution;

/*
 * This is the JUnit test for UniformDistribution
 * @author Quentin
 */
public class UniformDistributionTest {
	private double min;
	private double max;
	private UniformDistribution uniformDistribution;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		min = 0;
		max = 1;
		uniformDistribution = new UniformDistribution(min, max);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
	   uniformDistribution = null;
    }
	
	@Test
	public void testGenerateSample() {
		int nSample = 100000;
		double sumSample = 0;
		for (int i = 0; i <
				nSample; i++) {
			sumSample = sumSample + uniformDistribution.generateSample();
		}
		double average = sumSample/nSample;
		System.out.println(average);
		assertTrue(0.95*(min+max)/2 < average & 1.05*(min+max)/2 > average);
	}

}
