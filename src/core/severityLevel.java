package core;

/**
 * The subclasses of this class represent the levels of severity of a patient problem
 * @author Thomas
 *
 */
public abstract class severityLevel {
	protected String name;
	
	public severityLevel(String name) {
		this.name = name;
	}
}
