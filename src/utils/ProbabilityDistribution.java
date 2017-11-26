package utils;

/**
 * This interface implements a method to generate a sample from a given probability distribution.
 * @author Thomas
 *
 */
public interface ProbabilityDistribution {

	/**
	 * This method generates a sample from the probability distribution
	 * @return a sampled value
	 */
	public abstract double generateSample();
	
}
