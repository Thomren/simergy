package test;

import core.EmergencyDepartment;
import core.GoldInsurance;
import core.HealthInsurance;
import core.SeverityLevel;
import core.SeverityLevel_L1;
import resource.BloodTestRoom;
import resource.BoxRoom;
import resource.MRIRoom;
import resource.Nurse;
import resource.Patient;
import resource.Physician;
import resource.ShockRoom;
import resource.Transporter;
import resource.WaitingRoom;
import resource.XRayRoom;
import workflow.BloodTest;

/**
 * This class is a manual user case in order to test the first version of the app
 * @author Thomas
 *
 */
public class ManualUseCase {
	public static void main(String[] args) {
		System.out.println("Beginning of manual use case");
		// Create the ED
		EmergencyDepartment ED = new EmergencyDepartment("CentruleRupélec");
		// Add 5 waiting rooms with a capacity of 10
		WaitingRoom wr1 = new WaitingRoom("Waiting Room 1", 10, ED);
		ED.addRoom(wr1);
		WaitingRoom wr2 = new WaitingRoom("Waiting Room 2", 10, ED);
		ED.addRoom(wr2);
		WaitingRoom wr3 = new WaitingRoom("Waiting Room 3", 10, ED);
		ED.addRoom(wr3);
		WaitingRoom wr4 = new WaitingRoom("Waiting Room 4", 10, ED);
		ED.addRoom(wr4);
		WaitingRoom wr5 = new WaitingRoom("Waiting Room 5", 10, ED);
		ED.addRoom(wr5);
		// Add other rooms
		BoxRoom r1 = new BoxRoom("Box Room 1", 1, ED);
		ED.addRoom(r1);
		ShockRoom r2 = new ShockRoom("Shock Room 1", 1, ED);
		ED.addRoom(r2);
		XRayRoom r3 = new XRayRoom("XRay Room 1", 1, ED);
		ED.addRoom(r3);
		MRIRoom r4 = new MRIRoom("MRI Room 1", 1, ED);
		ED.addRoom(r4);
		BloodTestRoom r5 = new BloodTestRoom("Blood Test Room 1", 1, ED);
		ED.addRoom(r5);
		// Add 4 physicians
		Physician ph1 = new Physician("Henri", "Golo", "Physician1", ED);
		ED.addEmployee(ph1);
		Physician ph2 = new Physician("Paula", "Roïd", "Physician2", ED);
		ED.addEmployee(ph2);
		Physician ph3 = new Physician("Claire", "Hyère", "Physician3", ED);
		ED.addEmployee(ph3);
		Physician ph4 = new Physician("Harry", "Cover", "Physician4", ED);
		ED.addEmployee(ph4);
		// Add 4 nurses
		Nurse n1 = new Nurse("Lara", "Clette", ED);
		ED.addEmployee(n1);
		Nurse n2 = new Nurse("Barack", "Afritt", ED);
		ED.addEmployee(n2);
		Nurse n3 = new Nurse("Beth", "Rave", ED);
		ED.addEmployee(n3);
		Nurse n4 = new Nurse("Jean", "Bon", ED);
		ED.addEmployee(n4);
		// Add 4 transporters
		Transporter tr1 = new Transporter("Alain", "Térieur", ED);
		ED.addEmployee(tr1);
		Transporter tr2 = new Transporter("Alex", "Térieur", ED);
		ED.addEmployee(tr2);
		Transporter tr3 = new Transporter("Marie", "Age", ED);
		ED.addEmployee(tr3);
		Transporter tr4 = new Transporter("Lucie", "Fer", ED);
		ED.addEmployee(tr4);
		// Arrival of a patient
		Patient pat1 = new Patient("Jerry", "Kan", (HealthInsurance) new GoldInsurance(), (SeverityLevel) ED.getSeverityLevel(3), ED);
		ED.patientRegistration(pat1);
		// Triage
		ED.getService("Triage").executeServiceOnPatient(pat1);
		// Consultation
		ED.getService("Consultation").executeServiceOnPatient(pat1);
		// Transportation to test
		ED.getService("Transportation").executeServiceOnPatient(pat1);
		// Examination
		ED.getService("Consultation").executeServiceOnPatient(pat1);
		pat1.printReport();
		ED.printReport();
		System.out.println("Patient successfully treated and left hospital !");
		
	}
}
