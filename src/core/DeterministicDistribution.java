package core;

public class DeterministicDistribution implements ProbabilityDistribution {
	protected double value;
		
	public DeterministicDistribution(double value) {
		super();
		this.value = value;
	}

	@Override
	public double generateSample() {
		return value;
	}

}
