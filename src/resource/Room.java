package resource;

import java.util.ArrayList;

import core.EmergencyDepartment;
import core.Entity;

/**
 * Room is an abstract class to represent every room in the Emergency Department.
 * It has a capacity.
 * It inherits from Entity.
 * @author Quentin
 * @author Thomas
 *
 */

public abstract class Room extends Entity {
	protected int capacity;
	protected ArrayList<Patient> patients;
	
	public Room(String name, int capacity, EmergencyDepartment emergencyDepartment) {
		super(name, emergencyDepartment);
		this.capacity = capacity;
		this.patients = new ArrayList<Patient>();
	}

	/**
	 * This method return true if there is fewer patients than the capacity of the room, false if not
	 * @return
	 */
	public boolean isAvailable() {
		return capacity > patients.size();
	}
	
	public void addPatient(Patient patient) {
		if(isAvailable()) {
			patients.add(patient);
		}
		else {
			throw new RuntimeException("No available place in the room " + name);
		}
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}

	@Override
	public String toString() {
		return name;
	}

}
