package utils;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;

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
