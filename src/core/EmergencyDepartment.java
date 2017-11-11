package core;

import java.util.ArrayList;

import resource.Human;
import resource.Nurse;
import resource.Patient;
import resource.Physician;
import resource.Room;
import resource.Transporter;
import workflow.WorkflowElement;

/**
 * This class represents the whole emergency department. It stores all its entities and coordinates everything.
 * @author Thomas
 *
 */
public class EmergencyDepartment {
	protected ArrayList<Patient> patients;
	protected ArrayList<Room> rooms;
	protected ArrayList<Human> staff;
	protected ArrayList<Event> history;
	protected ArrayList<WorkflowElement> services;
	protected ArrayList<SeverityLevel> severityLevels;
	
	EmergencyDepartment() {
		patients = new ArrayList<Patient>();
		rooms = new ArrayList<Room>();
		staff = new ArrayList<Human>();
		history = new ArrayList<Event>();
		services = new ArrayList<WorkflowElement>();
		severityLevels = new ArrayList<SeverityLevel>();
	}
	
	/**
	 * This method searches an available room of a given type and return it if found, or null if there isn't one
	 * @param roomType
	 * @return an available room of the given type if there is one, null otherwise
	 */
	public Room getAvailableRoom(String roomType) {
		for (Room room: rooms) {
			try {
				if(Class.forName(roomType).isInstance(room) && room.isAvailable()) {
					return room;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * This method searches an idle physician among the staff and return it if found
	 * @return an idle physician if there is one, null otherwise
	 */
	public Physician getIdlePhysician() {
		for (Human employee: staff) {
			if(employee instanceof Physician && employee.getState() == "idle") {
				return (Physician) employee;
			}
		}
		return null;
	}
	
	/**
	 * This method searches an idle nurse among the staff and return it if found
	 * @return an idle nurse if there is one, null otherwise
	 */
	public Nurse getIdleNurse() {
		for (Human employee: staff) {
			if(employee instanceof Nurse && employee.getState() == "idle") {
				return (Nurse) employee;
			}
		}
		return null;
	}
	
	/**
	 * This method searches an idle transporter among the staff and return it if found
	 * @return an idle transporter if there is one, null otherwise
	 */
	public Transporter getIdleTransporter() {
		for (Human employee: staff) {
			if(employee instanceof Transporter && employee.getState() == "idle") {
				return (Transporter) employee;
			}
		}
		return null;
	}

	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}

	public void addPatient(Patient patient) {
		this.patients.add(patient);
	}
	
	public void removePatient(Patient patient) {
		this.patients.remove(patient);
	}
	
	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}

	public void addRoom(Room room) {
		this.rooms.add(room);
	}
	
	public void removeRoom(Room room) {
		this.rooms.remove(room);
	}
	
	public ArrayList<Human> getStaff() {
		return staff;
	}

	public void setStaff(ArrayList<Human> staff) {
		this.staff = staff;
	}
	
	public void addEmployee(Human employee) {
		this.staff.add(employee);
	}
	
	public void removeEmployee(Human employee) {
		this.staff.remove(employee);
	}

	public ArrayList<Event> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<Event> history) {
		this.history = history;
	}

	public void addEvent(Event event) {
		this.history.add(event);
	}
	
	public void removeEvent(Event event) {
		this.history.remove(event);
	}
	
	public ArrayList<WorkflowElement> getServices() {
		return services;
	}

	public void setServices(ArrayList<WorkflowElement> services) {
		this.services = services;
	}
	
	public void addService(WorkflowElement service) {
		this.services.add(service);
	}
	
	public void removeService(WorkflowElement service) {
		this.services.remove(service);
	}

	public ArrayList<SeverityLevel> getSeverityLevels() {
		return severityLevels;
	}

	public void setSeverityLevels(ArrayList<SeverityLevel> severityLevels) {
		this.severityLevels = severityLevels;
	}
	
	public void addSeverityLevel(SeverityLevel severityLevel) {
		this.severityLevels.add(severityLevel);
	}
	
	public void removeSeverityLevel(SeverityLevel severityLevel) {
		this.severityLevels.remove(severityLevel);
	}
}
