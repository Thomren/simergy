package core;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import processing.PatientArrival;
import processing.Task;
import resources.Nurse;
import resources.Patient;
import resources.Physician;
import resources.Room;
import resources.Transporter;
import utils.DeterministicDistribution;
import utils.ExponentialDistribution;
import utils.GoldInsurance;
import utils.NoInsurance;
import utils.SeverityLevel;
import utils.SilverInsurance;
import utils.UniformDistribution;
import workflow.WorkflowElement;

/**
 * This class is a CLUI in which you can enter commands to use the SimErgy simulator and see results of the simulation.
 * You can also load scenario files and output their result to a file. The list of command can be obtained by entering the
 * help command.
 * @author Thomas
 */
public class CLUI {
	private static HashMap<String, EmergencyDepartment> emergencyDepartments = new HashMap<String, EmergencyDepartment>();
	private static NurseFactory nurseFactory = new NurseFactory();
	private static PhysicianFactory physicianFactory = new PhysicianFactory();
	private static TransporterFactory transporterFactory = new TransporterFactory();
	
	/**
	 * This method set the probability distribution of the arrival of patients with a given severity level of an ED
	 * @param level whose probability distribution must be changed
	 * @param input is the command entered by the user
	 */
	private static void setSeverityLevelDistribution(int level, String[] input) {
		if(input.length < 4){
			System.out.println("Error: setL*arrivalDist requires 3 arguments <EDname, DistType, DistParams>");
		}
		else if(!emergencyDepartments.containsKey(input[1])) {
			System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
		}
		else {
			EmergencyDepartment ED = emergencyDepartments.get(input[1]);
			SeverityLevel severityLevel = ED.getSeverityLevel(level);
			try {
				switch(input[2]){
				case "uniform":
					if(input.length > 3){
						severityLevel.setProbabilityDistribution(new UniformDistribution(Double.parseDouble(input[3]), Double.parseDouble(input[4])));
					}
					else{
						severityLevel.setProbabilityDistribution(new UniformDistribution(Double.parseDouble(input[3])));
					}
					break;
				case "exponential":
					severityLevel.setProbabilityDistribution(new ExponentialDistribution(Double.parseDouble(input[3])));
					break;
				case "deterministic":
					severityLevel.setProbabilityDistribution(new DeterministicDistribution(Double.parseDouble(input[3])));
					break;
				default:
					System.out.println("Error: 2nd argument DistType must be exponential, deterministic or uniform");
				}
				System.out.println("Probability distribution of patient with severity level " + level + " successfuly set for " + input[1]);
			} catch(Exception e) {
				System.out.println("Error: Argument(s) <DistParam> must be double(s)");
			}
		}
	}
	
	/**
	 * This method set the duration of a given service in an ED
	 * @param serviceName is the name of the service whose duration must be changed
	 * @param input is the command entered by the user
	 */
	private static void setServiceDuration(String[] input) {
		if(input.length < 5){
			System.out.println("Error: setDuration requires 4 arguments <EDname, ServiceName, DistType, DistParams>");
		}
		else if(!emergencyDepartments.containsKey(input[1])) {
			System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
		}
		else {
			EmergencyDepartment ED = emergencyDepartments.get(input[1]);
			WorkflowElement service = ED.getService(input[2]);
			if(service == null){
				System.out.println("Error: the service " + input[2] + " doesn't exist");
			}
			try {
				switch(input[3]){
				case "uniform":
					if(input.length > 3){
						service.setDurationProbability((new UniformDistribution(Double.parseDouble(input[4]), Double.parseDouble(input[5]))));
					}
					else{
						service.setDurationProbability(new UniformDistribution(Double.parseDouble(input[4])));
					}
					break;
				case "exponential":
					service.setDurationProbability(new ExponentialDistribution(Double.parseDouble(input[4])));
					break;
				case "deterministic":
					service.setDurationProbability(new DeterministicDistribution(Double.parseDouble(input[4])));
					break;
				default:
					System.out.println("Error: 3rd argument DistType must be exponential, deterministic or uniform");
				}
				System.out.println("Duration probability distribution of " + input[2] + " successfuly set for " + input[1]);
			} catch(Exception e) {
				System.out.println("Error: Argument(s) <DistParam> must be double(s)");
			}
		}
	}
	
	/**
	 * This method set the cost of a given service in an ED
	 * @param serviceName is the name of the service whose cost must be changed
	 * @param input is the command entered by the user
	 */
	private static void setServiceCost(String[] input) {
		if(input.length < 4){
			System.out.println("Error: set<ServiceName>Cost requires 3 arguments <EDname, ServiceName, Cost>");
		}
		else if(!emergencyDepartments.containsKey(input[1])) {
			System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
		}
		else {
			EmergencyDepartment ED = emergencyDepartments.get(input[1]);
			WorkflowElement service = ED.getService(input[2]);
			if(service == null){
				System.out.println("Error: the service " + input[1] + " doesn't exist");
			}
			try {
				service.setCost(Double.parseDouble(input[3]));
				System.out.println("Cost of " + input[2] + " successfuly set to " + input[3] + " for " + input[1]);
			} catch(Exception e) {
				System.out.println("Error: Argument <Cost> must be a double");
			}
		}
	}
	
	/**
	 * This method processes the commands entered by the user
	 * @param input is the command entered by the user
	 */
	public static void processCommand(String[] input) {
		String command = input[0];
		// distinguishing between the chosen Exercise number 
		switch(command){
		
			case "createED":
				if(input.length < 2) {
					System.out.println("Error: Argument <name> compulsory but not found");
				}
				else {
					emergencyDepartments.put(input[1], new EmergencyDepartment(input[1]));
				}
				break;
				
			case "addRoom":
				if(input.length < 5){
					System.out.println("Error: addRoom requires 4 arguments <EDname, RoomType, RoomName, RoomCapacity>");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					int capacity;
					try {
						capacity = Integer.parseInt(input[4]);
						Class<?> roomClass = Class.forName("resources." + input[2]);
						Constructor<?> cons = roomClass.getConstructor(String.class, int.class, EmergencyDepartment.class);
						Room room = (Room) cons.newInstance(input[3], capacity, ED);
						ED.addRoom(room);
						System.out.println(input[2] + " " + input[3] + " of capacity " + input[4] + " successfully added to " + input[1]);
					} catch (NoSuchMethodException | SecurityException e) {
						System.out.println("Error: " + input[2] + " doesn't have the right constructor");
					} catch (ClassNotFoundException e) {
						System.out.println("Error: " + input[2] + " room type doesn't exist");
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						System.out.println("Error: 4th argument <capacity> must be an integer");
					}
				}
				break;
				
			case "addNurse":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					if(input.length < 3) {
						ED.addStaff(1, nurseFactory);
						System.out.println("Nurse successfully added to " + input[1]);
						
					}
					else {
						ED.addEmployee(new Nurse(input[2], input[3], ED));
						System.out.println("Nurse " + input[2] + " " + input[3] + " successfully added to " + input[1]);
					}
					
				}
				break;
				
			case "addPhysician":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					if(input.length < 3) {
						ED.addStaff(1, physicianFactory);
						System.out.println("Physician successfully added to " + input[1]);
						
					}
					else {
						ED.addEmployee(new Physician(input[2], input[3], ED));
						System.out.println("Physician " + input[2] + " " + input[3] + " successfully added to " + input[1]);
					}
				}
				break;
				
			case "addTransporter":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					if(input.length < 3) {
						ED.addStaff(1, transporterFactory);
						System.out.println("Transporter successfully added to " + input[1]);
						
					}
					else {
						ED.addEmployee(new Transporter(input[2], input[3], ED));
						System.out.println("Transporter " + input[2] + " " + input[3] + " successfully added to " + input[1]);
					}
				}
				break;
				
			case "setL1arrivalDist":
				setSeverityLevelDistribution(1, input);
				break;
				
			case "setL2arrivalDist":
				setSeverityLevelDistribution(2, input);
				break;
				
			case "setL3arrivalDist":
				setSeverityLevelDistribution(3, input);
				break;
			
			case "setL4arrivalDist":
				setSeverityLevelDistribution(4, input);
				break;
				
			case "setL5arrivalDist":
				setSeverityLevelDistribution(5, input);
				break;
				
			case "setDuration":
				setServiceDuration(input);
				break;
				
			case "setCost":
				setServiceCost(input);
				break;

			case "addPatient":
				if(input.length < 5){
					System.out.println("Error: addPatient requires 4 arguments <EDname, PatientName, PatientSurname, HealthInsurance>");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					Task nextPatientArrival = ED.getNextPatientArrival();
					nextPatientArrival.setTimestamp(ED.getTime());
					Patient patient = ((PatientArrival) nextPatientArrival.getCommand()).getPatient();
					patient.setName(input[2]);
					patient.setSurname(input[3]);
					switch(input[4]){
					case "gold":
						patient.setHealthInsurance(new GoldInsurance());
						break;
					case "silver":
						patient.setHealthInsurance(new SilverInsurance());
						break;
					case "none":
						patient.setHealthInsurance(new NoInsurance());
						break;
					default:
						System.out.println("Error: 4th argument <HealthInsurance> must be either 'gold', 'silver' or 'none'");
					}
					if(input.length == 6){
						try {
							int level = Integer.parseInt(input[5]);
							SeverityLevel severityLevel = ED.getSeverityLevel(level);
							patient.setSeverityLevel(severityLevel);
						} catch(Exception e) {
							System.out.println("Error: 5th argument <SeverityLevel> must be an integer between 1 and 5");
						}
					}
					nextPatientArrival.getCommand().execute();
					ED.getHistory().add(new Event(nextPatientArrival.getCommand().toString(), ED.getTime()));
					System.out.println("Patient " + input[2] + " " + input[3] + " with ID " + patient.getID() + " successfully arrived to " + input[1]);
				}
				break;
				
			case "executeEvents":
				if(input.length < 2){
					System.out.println("Error: executeEvents requires 2 arguments <EDname, NumberOfEvents>");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					try {
						int n = Integer.parseInt(input[2]);
						for(int i=0; i<n; i++) {
							ED.executeNextTask();
						}
					} catch (Exception e) {
						System.out.println("Error: 2nd argument NumberOfEvents must be an integer");
					}
				}
				break;
				
			case "simulate":
				if(input.length < 2){
					System.out.println("Error: simulate requires 2 arguments <EDname, DurationToSimulate>");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					try {
						double t = Double.parseDouble(input[2]);
						double beginning = ED.getTime();
						while(ED.getTime() - beginning < t) {
							ED.executeNextTask();
						}
						System.out.println("Hospital " + input[1] + " is now at time " + ED.getTime());
					} catch (Exception e) {
						System.out.println("Error: 2nd argument DurationToSimulate must be a double");
					}
				}
				break;
				
			case "kpi":
				if(input.length < 2){
					System.out.println("Error: kpi requires 2 arguments <EDname, KPIname>");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					try {
						System.out.println("Length-of-stay for hospital " + input[1] + " is " + ED.computeKPI((String)input[2]));
					} catch (Exception e) {
						System.out.println("Error: 2nd argument KPIname must be either los (Length-of-stay) or dtdt (Door-to-doctor-time)");
					}
				}
				break;
				
			case "executeEvent":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					ED.executeNextTask();
				}
				break;
			
			case "displayHistory":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					ED.printHistory();
				}
				break;
				
			case "display":
				if(input.length < 2){
					System.out.println("Error: Argument <EDname> compulsory but not found");
				}
				else if(!emergencyDepartments.containsKey(input[1])) {
					System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
				}
				else {
					EmergencyDepartment ED = emergencyDepartments.get(input[1]);
					if(input.length == 2){
						ED.printReport();
					}
					else {
						Patient targetedPatient = null;
						for(Patient patient: ED.getAllPatients()) {
							if(patient.getName().equals(input[2]) && patient.getSurname().equals(input[3])){
								targetedPatient = patient;
							}
						}
						if(targetedPatient != null) {
							targetedPatient.printReport();
						}
						else {
							System.out.println("Error: No patient named " + input[2] + " " + input[3] + " found in " + input[1]);
						}
					}
				}
				break;
				
			case "stop":
				System.out.println("---- Stopping the SimErgy terminal ---- ");
				break;
				
			case "list":
				Set<String> EDnames = emergencyDepartments.keySet();
				if(EDnames.size() > 0){
				System.out.println("The existing emergency departments are :");
					for (String EDname: emergencyDepartments.keySet()) {
						System.out.println("- " + EDname);
					}
				}
				else { System.out.println("There is no emergency department created. You can create one with createED <EDname>"); }
				break;
			
			case "help":
				System.out.println("The list of possible commands is:");
				System.out.println("\t stop: to quit this program");
				System.out.println("\t list: to display the list of created emergency departments");
				System.out.println("\t createED <EDname>: to create an emergency department");
				System.out.println("\t addRoom <EDname> <RoomType> <RoomName> <RoomCapacity>: to add a room to an ED");
				System.out.println("\t addNurse <EDname> [<NurseName>} [<NurseSurname>]: to add a nurse to an ED");
				System.out.println("\t addPhysician <EDname> [<PhysicianName>] [<PhysicianSurname>]: to add a physician to an ED");
				System.out.println("\t addPatient <EDname> <PatientName> <PatientSurname> <HealthInsurance> [<SeverityLevel>]: to add a patient to an ED");
				System.out.println("\t setL[1|2|3|4|5]arrivalDist <EDname> <DistType> <DistParam1> [<DistParam2>]: to set the distribution "
						+ "of patient arrivals of a given severity level in an ED");
				System.out.println("\t setDuration <EDname> <ServiceName> <DistType> <DistParam1> [<DistParam2>]: to set the distribution"
						+ "of probability of the duration of a service of an ED");
				System.out.println("\t setCost <EDname> <ServiceName> <Cost>: to set the cost of a service in an ED");
				System.out.println("\t executeEvent <EDname>: to execute the next event of an ED");
				System.out.println("\t executeEvents <EDname> <NumberOfEvents>: to execute the next NumberOfEvents events in an ED");
				System.out.println("\t kpi <EDname> <KPIname>: to calculate and display a KPI of an ED (los:Length-of-stay or dtdt:Door-to-doctor-time)");
				System.out.println("\t simulate <EDname> <time>: to simulate at least a certain time in an ED. The simulation stops just before"
						+ "the first event after currentTime + time");
				System.out.println("\t display <EDname> [<PatientName> <PatientSurname>]: to display the current state of an entire ED or"
						+ "of a patient of an ED");
				System.out.println("\t displayHistory <EDname>: to display the history an ED");
				System.out.println("\t runtest <Filename> [<OutputFilename>]: to run the commands in a file and optionally output the results to another file");
				break;
			default:
				System.out.println("The command " + command + " doesn't exist. Type help to see the list of possible commands or enter a valid one");
		}
	}
	
	/**
	 * This method execute the commands contained in a scenario file
	 * @param fileName is the filename of the scenario file to load
	 */
	public static void executeScenarioFile(String fileName) {
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(fileName); // a FileReader for reading byte by byte
			reader = new BufferedReader(file); // wrapping a FileReader into a BufferedReader for reading line by line
			String line = "";
			while ((line = reader.readLine()) != null) { // read the file line by line
				processCommand(splitInput(line));
			}
		} catch (Exception e) {
			System.out.println("Error: File " + filename + " not found");
		} finally {
			if (reader != null) {
				try {reader.close();}
				catch (IOException e) { }// Ignore issues
			}
			if (file != null) {
				try {file.close();}
				catch (IOException e) { }// Ignore issues during closing
			}
		}
	}
	
	/**
	 * This method split the command entered by the user into the command itself and the different arguments separated by spaces
	 * A argument can contain spaces if it is given between double quotes (ex: createED "St James Hospital")
	 * @param input is the input entered by the user
	 * @return an array of strings containing the command and the different arguments if any
	 */
	public static String[] splitInput(String input) {
		return input.split("\"?( |$)(?=(([^\"]*\"){2})*[^\"]*$)\"?");
	}
	
	/**
	 * This method starts the CLUI
	 * @param args
	 */
	public static void main(String[] args) {
		// declaring a scanner object for to ask input to the user
		Scanner sc = new Scanner(System.in);
		
		String[] input;
		String command = "";
		
		System.out.println("This is the SimErgy application - type \"help\" for a list of available commands or \"stop\"  to quit):");
		
		// the command-interpreter main loop: asking the user the next command to execute
		while(!command.equals("stop")){
			// asking the user what exercise he wants to solve		
			System.out.print(">> ");
			input = splitInput(sc.nextLine()); //Split arguments on spaces except if the space if between two double quotes
			command = input[0];
			if(command.contentEquals("runtest")) {
				if(input.length < 2){
					System.out.println("Error: Argument <Filename> is compulsory");
				} else if(input.length == 2) {
					executeScenarioFile(input[1]);
				} else { 
					try {
						System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(input[2])), true));
						executeScenarioFile(input[1]);
						System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
					} catch (FileNotFoundException e) {
						System.out.println("Error: File  " + input[2] + " cannot be created");
					}
				}
			}
			else {
				processCommand(input);
			}
		}
		sc.close();
		System.out.println("---- The SimErgy terminal stopped ---- ");	
	}
}
