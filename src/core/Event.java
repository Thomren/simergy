package core;

/**
 * This class represents an event happening to a patient
 * @author Thomas
 *
 */
public class Event {
	protected String name;
	protected double timestamp;
	
	public Event(String name, double timestamp) {
		super();
		this.name = name;
		this.timestamp = timestamp;
	}
}
