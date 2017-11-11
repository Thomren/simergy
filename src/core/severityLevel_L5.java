package core;

/**
 * This class represents a non-urgent level.
 * @author Thomas
 *
 */
public class SeverityLevel_L5 extends SeverityLevel {
	public SeverityLevel_L5(ProbabilityDistribution probabilityDistribution) {
		super("L5", 5, probabilityDistribution);
	}
}
