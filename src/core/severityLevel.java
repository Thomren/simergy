package core;

/**
 * The subclasses of this class represent the levels of severity of a patient problem
 * @author Thomas
 *
 */
public abstract class SeverityLevel {
	protected String name;
	protected int level;
	protected ProbabilityDistribution probabilityDistribution;
	
	public SeverityLevel(String name, int level, ProbabilityDistribution probabilityDistribution) {
		this.name = name;
		this.level = level;
		this.probabilityDistribution = probabilityDistribution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ProbabilityDistribution getProbabilityDistribution() {
		return probabilityDistribution;
	}

	public void setProbabilityDistribution(ProbabilityDistribution probabilityDistribution) {
		this.probabilityDistribution = probabilityDistribution;
	}
	
}
