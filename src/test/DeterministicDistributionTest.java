package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.DeterministicDistribution;

/*
 * This is the JUnit test for DeterministicDistribution
 * @author Quentin
 */

public class DeterministicDistributionTest {
	private Double lambda;
	private DeterministicDistribution deterministicDistribution;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("=== Initialisation ===");
		lambda = 0.0;
		deterministicDistribution = new DeterministicDistribution(lambda);
		System.out.println("=== End of initialisation ===");
	}
	
   @After
    public void tearDown() {
	   deterministicDistribution = null;
    }
	
	@Test
	public void testGenerateSample() {
		assertTrue(deterministicDistribution.generateSample() == lambda);
	}
	
	@Test
	public void testNotGenerateSample() {
		assertFalse(deterministicDistribution.generateSample() == lambda+1);
	}
}
