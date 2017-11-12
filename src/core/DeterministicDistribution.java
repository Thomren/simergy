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
	
	public static void main(String[] args) {
		DeterministicDistribution dist = new DeterministicDistribution(2);
		for (int i = 0; i < 15; i++) {
			System.out.println(dist.generateSample());
		}
	}

}
