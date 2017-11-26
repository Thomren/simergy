package utils;

/**
 * This class represents a less urgent severity level.
 * @author Thomas
 *
 */
public class SeverityLevel_L4 extends SeverityLevel {
	public SeverityLevel_L4(ProbabilityDistribution probabilityDistribution) {
		super("L4", 4, probabilityDistribution);
	}
}
