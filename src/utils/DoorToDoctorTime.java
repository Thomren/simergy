package utils;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;

/**
 * This class herits from KeyPerformanceIndicator.
 * It implements computeKPI to compute the duration between a patient's arrival and his first consultation.
 * @author Quentin
 *
 */

public class DoorToDoctorTime extends KeyPerformanceIndicator {

	public DoorToDoctorTime(EmergencyDepartment emergencyDepartment) {
		super(emergencyDepartment);
	}

	@Override
	public double computeKPI(Patient patient) {
		for (Event event : patient.getHistory()) {
			if (event.getName() == "Consultation beginning") {
				return event.getTimestamp() - patient.getArrivalTime();
			}
		}
		return Double.POSITIVE_INFINITY;
	}
}
