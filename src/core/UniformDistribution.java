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
		return min + Math.random() * ( max - min );
	}
	
	public static void main(String[] args) {
		UniformDistribution dist = new UniformDistribution(2, 5);
		for (int i = 0; i < 15; i++) {
			System.out.println(dist.generateSample());
		}
	}
	
}
