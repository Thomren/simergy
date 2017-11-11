package resource;

import core.EmergencyDepartment;
import core.Entity;

/**
 * The Human abstract class add a surname property to Entity so as to represents Humans
 * @author Thomas
 *
 */
public abstract class Human extends Entity {
	protected String surname;
	protected String state;

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
		
}
