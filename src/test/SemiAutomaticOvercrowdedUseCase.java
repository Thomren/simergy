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
import utils.ExponentialDistribution;
import utils.SeverityLevel;
import utils.SeverityLevel_L1;
import utils.SeverityLevel_L2;
import utils.SeverityLevel_L3;
import utils.SeverityLevel_L4;
import utils.SeverityLevel_L5;

/**
 * This class is a manual user case in order to test the first version of the app
 * @author Quentin
 *
 */
public class SemiAutomaticOvercrowdedUseCase {
	public static void main(String[] args) {
		System.out.println("Beginning of manual use case");
		// Create the ED
		EmergencyDepartment ED = new EmergencyDepartment("CentruleRupelec");
		SeverityLevel[] severityLevels = new SeverityLevel[] {
				new SeverityLevel_L1(new ExponentialDistribution(0.01)),
				new SeverityLevel_L2(new ExponentialDistribution(0.05)),
				new SeverityLevel_L3(new ExponentialDistribution(0.08)),
				new SeverityLevel_L4(new ExponentialDistribution(0.1)),
				new SeverityLevel_L5(new ExponentialDistribution(0.2))
		};
		ED.setSeverityLevels(severityLevels);
		// Add 1 waiting rooms with a capacity of 10
		WaitingRoom wr1 = new WaitingRoom("Waiting Room 1", 10, ED);
		ED.addRoom(wr1);
		// Add other rooms
		BoxRoom br1 = new BoxRoom("Box Room 1", 1, ED);
		ED.addRoom(br1);
		ShockRoom sr1 = new ShockRoom("Shock Room 1", 1, ED);
		ED.addRoom(sr1);
		BoxRoom br2 = new BoxRoom("Box Room 2", 1, ED);
		ED.addRoom(br2);
		ShockRoom sr2 = new ShockRoom("Shock Room 2", 1, ED);
		ED.addRoom(sr2);
		XRayRoom r3 = new XRayRoom("XRay Room 1", 1, ED);
		ED.addRoom(r3);
		MRIRoom r4 = new MRIRoom("MRI Room 1", 1, ED);
		ED.addRoom(r4);
		BloodTestRoom r5 = new BloodTestRoom("Blood Test Room 1", 1, ED);
		ED.addRoom(r5);
		// Add 2 physicians
		PhysicianFactory physicianFactory = new PhysicianFactory();
		ED.addStaff(2, physicianFactory);
		// Add 4 nurses
		NurseFactory nurseFactory = new NurseFactory();
		ED.addStaff(4, nurseFactory);
		// Add 4 transporters
		TransporterFactory transporterFactory = new TransporterFactory();
		ED.addStaff(4, transporterFactory);
		// Arrival of a patient
		ED.getNextPatientArrival().getCommand().execute();
		Patient patient = ED.getPatients().get(0);
		for (int i = 0; i < 10000; i++) {
			ED.executeNextTask();
		}
		patient.printReport();
		ED.printReport();
	}
}
