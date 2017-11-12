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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return timestamp + " : " + name;
	}
	
}
