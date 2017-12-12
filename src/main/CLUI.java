package main;

import java.util.Scanner;

import core.EmergencyDepartment;
import fr.ecp.is1220.tutorial5.ex2.Node;

public class CLUI {
	private EmergencyDepartment emergencyDepartment;
	
	public static void main(String[] args) {
		// declaring a scanner object for to ask input to the user
		Scanner sc = new Scanner(System.in);
		
		// the buffer  to store inputed commands are stored
		String command = "";
			
		// the buffer to store arguments of inputed commands
		String argument = "";
		
		System.out.println("This is the SimErgy application - type \"help\" for a list of available commands or \"stop\"  to quit):");
		
		// the command-interpreter main loop: asking the user the next command to execute
		while(!command.equals("stop")){
			// asking the user what exercise he wants to solve		
			System.out.println(">> type in a command (type \"help\" for a list of available commands): ");
			
			command = in.next();
			// distinguishing between the chosen Exercise number 
			switch(command){
			
				case "createED":
					break;
					
				case "addRoom":
					
				case "addRadioService":
					
				case "addMRI":
					
				case "addBloodTest":
					
				case "addNurse":
					
				case "addPhysician":
					
				case "setL1arrivalDist":
					
				case "setL2arrivalDist":
					
				case "setL3arrivalDist":
				
				case "setL4arrivalDist":
					
				case "setL5arrivalDist":
					
				case "addPatient":
					
				case "registerPatient":
					
				case "setPatientInsurance":
					
				case "executeEvent":
					
				case "display":
					
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
		}
		System.out.println("---- The SimErgy terminal stopped ---- ");		
	}

}
