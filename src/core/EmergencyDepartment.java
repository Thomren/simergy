package core;

import java.util.ArrayList;
import java.util.Arrays;

import resource.Human;
import resource.Nurse;
import resource.Patient;
import resource.Physician;
import resource.Room;
import resource.Transporter;
import resource.WaitingRoom;
import workflow.BloodTest;
import workflow.Consultation;
import workflow.Installation;
import workflow.MRI;
import workflow.Transportation;
import workflow.Triage;
import workflow.WorkflowElement;
import workflow.XRay;

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
	protected WorkflowElement[] services;
	protected SeverityLevel[] severityLevels;
	protected double time;
	protected String name;
	
	public EmergencyDepartment(String name) {
		this.name = name;
		patients = new ArrayList<Patient>();
		rooms = new ArrayList<Room>();
		staff = new ArrayList<Human>();
		history = new ArrayList<Event>();
		services = new WorkflowElement[] {
				new BloodTest((ProbabilityDistribution) new DeterministicDistribution(5), 15., this),
				new Consultation("Consultation", (ProbabilityDistribution) new DeterministicDistribution(2), 5., this),
				new Installation("Installation", (ProbabilityDistribution) new DeterministicDistribution(5), this, 0.),
				new MRI((ProbabilityDistribution) new DeterministicDistribution(10), 20., this),
				new Transportation("Transportation", (ProbabilityDistribution) new DeterministicDistribution(5), this, 0.),
				new Triage("Triage", (ProbabilityDistribution) new DeterministicDistribution(1), this, 0.),
				new XRay((ProbabilityDistribution) new DeterministicDistribution(15), 50., this)};
		severityLevels = new SeverityLevel[] {
				new SeverityLevel_L1(new ExponentialDistribution(0.01)),
				new SeverityLevel_L2(new ExponentialDistribution(0.05)),
				new SeverityLevel_L3(new ExponentialDistribution(0.1)),
				new SeverityLevel_L4(new ExponentialDistribution(0.2)),
				new SeverityLevel_L5(new ExponentialDistribution(0.5))
		};
		time = 0;
		System.out.println("Hospital " + name + " successfully created !");
	}
	
	/**
	 * Register a patient in the ED system and put him in a waiting room
	 * @param patient to register
	 */
	public void patientRegistration(Patient patient) {
		Room waitingRoom = getAvailableRoom("WaitingRoom");
		if (waitingRoom != null) {
			patient.setLocation(waitingRoom);
			waitingRoom.addPatient(patient);
			patient.setArrivalTime(time);
			patient.addEvent(new Event("Arrival", time));
			addPatient(patient); // Add the patient in the ED
			getService("Triage").addPatientToWaitingList(patient);
		}
	}
	
	/**
	 * This method searches an available room of a given type and return it if found, or null if there isn't one
	 * @param roomType
	 * @return an available room of the given type if there is one, null otherwise
	 */
	public Room getAvailableRoom(String roomType) {
		for (Room room: rooms) {
			try {
				if(Class.forName("resource." + roomType).isInstance(room) && room.isAvailable()) {
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

	/**
	 * This method return a service of the hospital by name
	 * @param serviceName the wanted service
	 * @return the searched service
	 */
	public WorkflowElement getService(String serviceName) {
		for (WorkflowElement service: services) {
			try {
				if(Class.forName("workflow." + serviceName).isInstance(service)) {
					return service;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
	
	public WorkflowElement[] getServices() {
		return services;
	}

	public void setServices(WorkflowElement[] services) {
		this.services = services;
	}

	public SeverityLevel[] getSeverityLevels() {
		return severityLevels;
	}

	public void setSeverityLevels(SeverityLevel[] severityLevels) {
		this.severityLevels = severityLevels;
	}
	
	public SeverityLevel getSeverityLevel(int level) {
		return severityLevels[level - 1];
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "EmergencyDepartment " + name;
	}
	
	/**
	 * This method print all the informations about the Emergency Department and
	 * all its content
	 */
	public void printReport() {
		StringBuffer content = new StringBuffer();
		content.append("\n----- Emergency Department Report -----\n");
		content.append(this.toString() + "\n");
		content.append("Current time : " + time + "\n");
		content.append("• Patients : \n");
		for (Patient patient : patients) {
			content.append(patient).append('\n');
		}
		content.append("• Rooms : \n");
		for (Room room : rooms) {
			content.append(room.toStringDetailed()).append('\n');
		}
		content.append("• Staff : \n");
		for (Human employee : staff) {
			if (employee instanceof Physician) {
				content.append(((Physician) employee).toStringDetailed() + "\n");
			}
			else {
				content.append(employee).append('\n');
			}
		}
		content.append("• Services : \n");
		for (WorkflowElement service : services) {
			content.append(service.toStringDetailed()).append('\n');
		}
		content.append("• History : \n");
		for (Event event : history) {
			content.append(event).append('\n');
		}
		System.out.println(content.toString());
	}
	
	
	
}
