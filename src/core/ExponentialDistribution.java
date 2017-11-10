package core;

/**
 * This class simulates an exponential probability distribution
 * @author Thomas
 *
 */
public class ExponentialDistribution implements ProbabilityDistribution {
	protected double lambda;
	
	public ExponentialDistribution(double lambda) {
		super();
		this.lambda = lambda;
	}

	@Override
	public double generateSample() {
		return (-1) * Math.log(Math.random()) / lambda;
	}

}
