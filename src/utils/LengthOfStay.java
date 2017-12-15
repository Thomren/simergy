package utils;

import core.EmergencyDepartment;
import core.Event;
import resources.Patient;

/**
 * This class herits from KeyPerformanceIndicator.
 * It implements computeKPI to compute the duration between a patient's arrival and his release.
 * @author Quentin
 *
 */
public class LengthOfStay extends KeyPerformanceIndicator {

	public LengthOfStay(EmergencyDepartment emergencyDepartment) {
		super(emergencyDepartment);
	}

	@Override
	public double computeKPI(Patient patient) {
		Event lastEvent = patient.getHistory().get(patient.getHistory().size()-1);
		if(lastEvent.getName().equals("Released")) {
			return lastEvent.getTimestamp() - patient.getArrivalTime();
		}
		return Double.POSITIVE_INFINITY;
	}
	
	

}
