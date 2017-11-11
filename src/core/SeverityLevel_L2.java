package core;

/**
 * This class represents a situation of emergency.
 * @author Thomas
 *
 */
public class SeverityLevel_L2 extends SeverityLevel {
	public SeverityLevel_L2(ProbabilityDistribution probabilityDistribution) {
		super("L2", 2, probabilityDistribution);
	}
}
