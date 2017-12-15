package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import core.EmergencyDepartment;
import core.Event;
import core.NurseFactory;
import core.PhysicianFactory;
import processing.PatientArrival;
import processing.Task;
import resources.Nurse;
import resources.Patient;
import resources.Room;
import utils.DeterministicDistribution;
import utils.ExponentialDistribution;
import utils.GoldInsurance;
import utils.NoInsurance;
import utils.SeverityLevel;
import utils.SilverInsurance;
import utils.UniformDistribution;

public class CLUI {
	private static HashMap<String, EmergencyDepartment> emergencyDepartments = new HashMap<String, EmergencyDepartment>();
	private static NurseFactory nurseFactory = new NurseFactory();
	private static PhysicianFactory physicianFactory = new PhysicianFactory();
	
	private static void setSeverityLevelDistribution(int level, String[] input) {
		if(input.length < 3){
			System.out.println("Error: setL1arrivalDist requires 3 arguments <EDname, DistType, DistParams>");
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
					System.out.println("Error: 2nd argument DistType must be exponentiel, deterministic or uniform");
				}
				System.out.println("Probability distribution of patient with severity level " + level + " successfuly set for " + input[1]);
			} catch(Exception e) {
				System.out.println("Error: Argument(s) <DistParam> must be double(s)");
			}
		}
	}
	
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
			input = sc.nextLine().split(" ");
			command = input[0];
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
					
				case "addRadioService":
					break;
					
				case "addMRI":
					break;
					
				case "addBloodTest":
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
							ED.addEmployee(new Nurse(input[2], input[3], ED));
							System.out.println("Physician " + input[2] + " " + input[3] + " successfully added to " + input[1]);
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
					
				case "addPatient":
					if(input.length < 5){
						System.out.println("Error: addPatient requires 4 arguments <EDname, PatientName, PatientSurname, HealthInsurance");
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
							while(ED.getTime() < t) {
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
					
				case "display":
					if(input.length < 1){
						System.out.println("Error: Argument <EDname> compulsory but not found");
					}
					else if(!emergencyDepartments.containsKey(input[1])) {
						System.out.println("Error: There is no Emergency Department called " + input[1] + ". You can create it with createED " + input[1]);
					}
					else {
						EmergencyDepartment ED = emergencyDepartments.get(input[1]);
						ED.printReport();
					}
					break;
					
				case "stop":
					System.out.println("---- Stopping the SimErgy terminal ---- ");
					break;
				
				case "help":
					System.out.println("The list of possible commands is:");
					System.out.println("\t stop: to quit this program");
					System.out.println("\t list: to display the list of stations in the loaded metro network");
					System.out.println("\t connect [station1] [station2]: to display the shortest path connecting station1 to station2");
					System.out.println("\t succ [source-stattion]: to display the list of successor station of source-station");
					System.out.println("\t loadmap: to load the map-file for a metro network");
					System.out.println("\t boston: to load the map-file for Boston metro network (equivalent to 'loadmap bostonmetro.txt')");
					break;
				default:
					System.out.println("The command " + command + " doesn't exist. Type help to see the list of possible commands or enter a valid one");
			}
		}
		System.out.println("---- The SimErgy terminal stopped ---- ");	
	}
}
