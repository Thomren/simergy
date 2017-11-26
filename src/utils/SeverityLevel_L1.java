package utils;

/**
 * This class represent the most urgent situation in which the patient almost need to be resuscitated.
 * @author Thomas
 *
 */
public class SeverityLevel_L1 extends SeverityLevel {
	public SeverityLevel_L1(ProbabilityDistribution probabilityDistribution) {
		super("L1", 1, probabilityDistribution);
	}
}
