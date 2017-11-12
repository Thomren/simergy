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
	
	public static void main(String[] args) {
		ExponentialDistribution dist = new ExponentialDistribution(0.5);
		for (int i = 0; i < 15; i++) {
			System.out.println(dist.generateSample());
		}
	}

}
