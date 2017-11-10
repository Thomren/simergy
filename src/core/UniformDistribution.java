package core;

/**
 * This class simulates a Uniform probability distribution
 * @author Thomas
 *
 */
public class UniformDistribution implements ProbabilityDistribution {
	protected double min;
	protected double max;
	
	public UniformDistribution(double max) {
		super();
		this.min = 0;
		this.max = max;
	}

	public UniformDistribution(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}

	@Override
	public double generateSample() {
		return Math.random() * ( max - min );
	}
	
}
