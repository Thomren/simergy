package resource;

import java.util.ArrayList;

import core.EmergencyDepartment;
import core.Entity;
import core.Event;

/**
 * The Human abstract class add a surname property to Entity so as to represents Humans
 * @author Thomas
 *
 */
public abstract class Human extends Entity {
	protected String surname;
	protected String state;
	protected ArrayList<Event> history;

	public Human(String name, String surname, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.surname = surname;
		this.state = "idle";
	}

	public Human(String name, String surname, String state, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.surname = surname;
		this.state = state;
	}
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public ArrayList<Event> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<Event> history) {
		this.history = history;
	}

	public void addEvent(Event event) {
		this.history.add(event);
		System.out.println(this.toString() + "'s event : " + event.getName());
	}
	
	/**
	 * This method returns the time of the last entry in the history.
	 * @return double
	 */
	public Double getHistoryTime() {
		return this.history.get(this.history.size()-1).getTimestamp();
		
	}
		
}
