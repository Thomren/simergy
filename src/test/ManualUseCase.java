package test;

import core.EmergencyDepartment;
import core.NurseFactory;
import core.PhysicianFactory;
import core.TransporterFactory;
import resources.BloodTestRoom;
import resources.BoxRoom;
import resources.MRIRoom;
import resources.Patient;
import resources.ShockRoom;
import resources.WaitingRoom;
import resources.XRayRoom;

/**
 * This class is a manual user case in order to test the first version of the app
 * @author Thomas
 *
 */
public class ManualUseCase {
	public static void main(String[] args) {
		System.out.println("Beginning of manual use case");
		// Create the ED
		EmergencyDepartment ED = new EmergencyDepartment("CentruleRupelec");
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
		PhysicianFactory physicianFactory = new PhysicianFactory();
		ED.addStaff(4, physicianFactory);
		// Add 4 nurses
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(4, nurseFactory);
		// Add 4 transporters
		TransporterFactory transporterFactory = new TransporterFactory();
		ED.addStaff(4, transporterFactory);
		// Arrival of a patient
		ED.getNextPatientArrival().getCommand().execute();
		Patient patient = ED.getPatients().get(0);
		for (int i = 0; i < 15; i++) {
			ED.executeNextTask();
		}
		patient.printReport();
		ED.printReport();
		System.out.println("Patient successfully treated and left hospital !");
	}
}
