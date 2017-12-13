package utils;

import java.util.ArrayList;

import core.EmergencyDepartment;
import resources.Patient;

public abstract class KeyPerformanceIndicator {
	protected EmergencyDepartment emergencyDepartment;

	public KeyPerformanceIndicator(EmergencyDepartment emergencyDepartment) {
		super();
		this.emergencyDepartment = emergencyDepartment;
	}

	public double computeAverageKPI() {
		ArrayList<Patient> patients = emergencyDepartment.getPatients();
		if(patients.size() == 0) {
			return 0;
		}
		else {
			double sumKPI = 0.0;
			int numberPatientsReleased = 0;
			for (Patient patient : patients) {
				double KPI = computeKPI(patient);
				if(!(Double.isInfinite(KPI))) {
					sumKPI += KPI;
					numberPatientsReleased++;
				}
			}
			return sumKPI/numberPatientsReleased;
		}
	}
	
	public abstract double computeKPI(Patient patient);
	
}
