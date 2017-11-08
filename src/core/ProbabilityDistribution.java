package core;

public abstract class ProbabilityDistribution {
	protected String name;
	protected Double[] parameters;
	
	public ProbabilityDistribution(String name, Double[] parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}
	
}
