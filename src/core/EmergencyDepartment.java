package core;

import java.util.ArrayList;
import processing.PatientArrival;
import processing.Task;
import processing.TasksQueue;
import resources.Corridor;
import resources.Human;
import resources.Nurse;
import resources.Patient;
import resources.Physician;
import resources.Room;
import resources.Transporter;
import utils.DeterministicDistribution;
import utils.DoorToDoctorTime;
import utils.ExponentialDistribution;
import utils.KeyPerformanceIndicator;
import utils.LengthOfStay;
import utils.SeverityLevel;
import utils.SeverityLevel_L1;
import utils.SeverityLevel_L2;
import utils.SeverityLevel_L3;
import utils.SeverityLevel_L4;
import utils.SeverityLevel_L5;
import utils.UniformDistribution;
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
	protected ArrayList<Patient> releasedPatients;
	protected ArrayList<Room> rooms;
	protected Corridor corridor;
	protected ArrayList<Human> staff;
	protected ArrayList<Event> history;
	protected WorkflowElement[] services;
	protected SeverityLevel[] severityLevels;
	protected double time;
	protected String name;
	protected PatientFactory patientFactory;
	protected double[] nextPatientsTimestamp;
	
	public EmergencyDepartment(String name) {
		this.name = name;
		patients = new ArrayList<Patient>();
		releasedPatients = new ArrayList<Patient>();
		rooms = new ArrayList<Room>();
		corridor = new Corridor(this);
		staff = new ArrayList<Human>();
		history = new ArrayList<Event>();
		services = new WorkflowElement[] {
				new Triage(new UniformDistribution(2,5), 0., this),
				new Installation(new DeterministicDistribution(5), 0., this),
				new Consultation(new UniformDistribution(5,20), 15., this),
				new Transportation(new DeterministicDistribution(5), 0., this),
				new BloodTest(new UniformDistribution(15,90), 25., this),
				new MRI(new UniformDistribution(30,70), 30., this),
				new XRay(new UniformDistribution(10,20), 20., this)};
		severityLevels = new SeverityLevel[] {
				new SeverityLevel_L1(new ExponentialDistribution(0.001)),
				new SeverityLevel_L2(new ExponentialDistribution(0.005)),
				new SeverityLevel_L3(new ExponentialDistribution(0.01)),
				new SeverityLevel_L4(new ExponentialDistribution(0.02)),
				new SeverityLevel_L5(new ExponentialDistribution(0.05))
		};
		time = 0;
		patientFactory = new PatientFactory();
		nextPatientsTimestamp = new double[severityLevels.length];
		for (int i = 0; i < nextPatientsTimestamp.length; i++) {
			nextPatientsTimestamp[i] = severityLevels[i].getProbabilityDistribution().generateSample();
		}
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
		Task task = queue.executeNextTask();
		this.time = task.getTimestamp();
		this.history.add(new Event(task.getCommand().toString(), this.time));
	}
	
	/**
	 * Calculate the time of the next arrival of a patient, generate the patient and return
	 * the corresponding task
	 * @return the task corresponding to the arrival of the next patient
	 */
	public Task getNextPatientArrival() {
		int argmin = -1;
		for (int i = 0; i < severityLevels.length; i++) {
			if(i == 0 || nextPatientsTimestamp[i] < nextPatientsTimestamp[argmin]) {
				argmin = i;
			}
		}
		Patient patient = patientFactory.create(severityLevels[argmin], this);
		PatientArrival patientArrival = new PatientArrival(patient, this);
		return new Task(nextPatientsTimestamp[argmin], patientArrival);
	}

	/**
	 * Add a patient in the Emergency Departement and put him in a waiting room (waiting for registration)
	 * @param patient to register
	 */
	public void patientArrival(Patient patient) {
		Room waitingRoom = getAvailableRoom("WaitingRoom");
		int level = patient.getSeverityLevel().getLevel();
		nextPatientsTimestamp[level-1] = nextPatientsTimestamp[level-1] + severityLevels[level-1].getProbabilityDistribution().generateSample();
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
	 * This method computes the given KPI of the emergency department
	 * @param category a string representing the category of KPI wanted by the user
	 * @return the average KPI of the emergency department if the String is correct, an Exception otherwise
	 */
	public double computeKPI(String category) throws Exception {
		KeyPerformanceIndicator KPI;
		if (category.contentEquals("los")) {
			KPI = new LengthOfStay(this);
		}
		else if (category.contentEquals("dtdt")) {
			KPI = new DoorToDoctorTime(this);
		}
		else {
			throw new Exception();
		}
		return KPI.computeAverageKPI();
	}
	
	/**
	 * This method searches an available room of a given type and return it if found, or null if there isn't one
	 * @param roomType a string representing the type of the room
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
		if (roomType == "WaitingRoom") {
			return corridor;
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
		content.append(corridor.toStringDetailed()).append('\n');
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
//		content.append("- History : \n");
//		for (Event event : history) {
//			content.append(event).append('\n');
//		}
		content.append("- KPI : \n");
		content.append("  * Lenght of stay : ");
		try {
			content.append(this.computeKPI("los")).append('\n');
		}
		catch (Exception e) {
			System.out.println(e);
		}
		content.append("  * Door to doctor time : ");
		try {
			content.append(this.computeKPI("dtdt")).append('\n');
		}
		catch (Exception e) {
			System.out.println(e);
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
		this.releasedPatients.add(patient);
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

	public double[] getNextPatientsTimestamp() {
		return nextPatientsTimestamp;
	}

	public void setNextPatientsTimestamp(double[] nextPatientsTimestamp) {
		this.nextPatientsTimestamp = nextPatientsTimestamp;
	}
	
	public ArrayList<Patient> getAllPatients() {
		ArrayList<Patient> allPatients = new ArrayList<Patient>();
		allPatients.addAll(this.patients);
		allPatients.addAll(this.releasedPatients);
		return allPatients;
	}
	
}
