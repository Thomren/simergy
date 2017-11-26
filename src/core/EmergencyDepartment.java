package core;

import java.util.ArrayList;
import processing.PatientArrival;
import processing.Task;
import processing.TasksQueue;
import resources.Human;
import resources.Nurse;
import resources.Patient;
import resources.Physician;
import resources.Room;
import resources.Transporter;
import utils.DeterministicDistribution;
import utils.ExponentialDistribution;
import utils.ProbabilityDistribution;
import utils.SeverityLevel;
import utils.SeverityLevel_L1;
import utils.SeverityLevel_L2;
import utils.SeverityLevel_L3;
import utils.SeverityLevel_L4;
import utils.SeverityLevel_L5;
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
	protected PatientFactory patientFactory;
	
	public EmergencyDepartment(String name) {
		this.name = name;
		patients = new ArrayList<Patient>();
		rooms = new ArrayList<Room>();
		staff = new ArrayList<Human>();
		history = new ArrayList<Event>();
		services = new WorkflowElement[] {
				new BloodTest((ProbabilityDistribution) new DeterministicDistribution(5), 15., this),
				new Consultation((ProbabilityDistribution) new DeterministicDistribution(2), 5., this),
				new Installation((ProbabilityDistribution) new DeterministicDistribution(5), this, 0.),
				new MRI((ProbabilityDistribution) new DeterministicDistribution(10), 20., this),
				new Transportation((ProbabilityDistribution) new DeterministicDistribution(5), this, 0.),
				new Triage((ProbabilityDistribution) new DeterministicDistribution(1), this, 0.),
				new XRay((ProbabilityDistribution) new DeterministicDistribution(15), 50., this)};
		severityLevels = new SeverityLevel[] {
				new SeverityLevel_L1(new ExponentialDistribution(0.01)),
				new SeverityLevel_L2(new ExponentialDistribution(0.05)),
				new SeverityLevel_L3(new ExponentialDistribution(0.1)),
				new SeverityLevel_L4(new ExponentialDistribution(0.2)),
				new SeverityLevel_L5(new ExponentialDistribution(0.5))
		};
		time = 0;
		patientFactory = new PatientFactory();
		System.out.println("Hospital " + name + " successfully created !");
	}
	
	/**
	 * This method searches the next task to execute among the services of the Emergency Department
	 * and the next patient arrival, then executes it and update the global time.
	 */
	public void executeNextTask() {
		TasksQueue queue = new TasksQueue();
		for (WorkflowElement service : services) {
			try {
				queue.addTask(service.getNextTask());
			}
			catch (NullPointerException e) {
			}
		}
		queue.addTask(this.getNextPatientArrival());
		double nextTime = queue.executeNextTask();
		this.time = nextTime;
	}
	
	/**
	 * Calculate the time of the next arrival of a patient, generate the patient and return
	 * the corresponding task
	 * @return the task corresponding to the arrival of the next patient
	 */
	public Task getNextPatientArrival() {
		double minTimestamp = -1;
		SeverityLevel minSeverityLevel = null;
		for (int i = 0; i < severityLevels.length; i++) {
			double timestamp = severityLevels[i].getProbabilityDistribution().generateSample();
			if(i == 0 || timestamp < minTimestamp) {
				minTimestamp = timestamp;
				minSeverityLevel = severityLevels[i];
			}
		}
		Patient patient = patientFactory.create(minSeverityLevel, this);
		PatientArrival patientArrival = new PatientArrival(patient, this);
		return new Task(minTimestamp, patientArrival);
	}

	/**
	 * Add a patient in the Emergency Departement and put him in a waiting room (waiting for registration)
	 * @param patient to register
	 */
	public void patientArrival(Patient patient) {
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
				if(Class.forName("resources." + roomType).isInstance(room) && room.isAvailable()) {
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
	
	/**
	 * Add staff members to the emergency department given a factory
	 * @param n number of staff members to add
	 * @param factory which product staff members type you want to add
	 */
	public void addStaff(int n, HumanFactory factory) {
		for (int i = 0; i < n; i++) {
			staff.add(factory.create(this));
		}
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
		content.append("- Patients : \n");
		for (Patient patient : patients) {
			content.append(patient).append('\n');
		}
		content.append("- Rooms : \n");
		for (Room room : rooms) {
			content.append(room.toStringDetailed()).append('\n');
		}
		content.append("- Staff : \n");
		for (Human employee : staff) {
			if (employee instanceof Physician) {
				content.append(((Physician) employee).toStringDetailed() + "\n");
			}
			else {
				content.append(employee).append('\n');
			}
		}
		content.append("- Services : \n");
		for (WorkflowElement service : services) {
			content.append(service.toStringDetailed()).append('\n');
		}
		content.append("- History : \n");
		for (Event event : history) {
			content.append(event).append('\n');
		}
		System.out.println(content.toString());
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
	
	public void addRooms(Room[] rooms) {
		for (Room room : rooms) {
			this.rooms.add(room);
		}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PatientFactory getPatientFactory() {
		return patientFactory;
	}

	public void setPatientFactory(PatientFactory patientFactory) {
		this.patientFactory = patientFactory;
	}


}
