package core;

/**
 * The subclasses of this class represent the levels of severity of a patient problem
 * @author Thomas
 *
 */
public abstract class SeverityLevel {
	protected String name;
	
	public SeverityLevel(String name) {
		this.name = name;
	}
}
