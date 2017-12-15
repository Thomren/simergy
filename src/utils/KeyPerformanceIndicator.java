package utils;

import java.util.ArrayList;

import core.EmergencyDepartment;
import resources.Patient;

/**
 * This is an abstract class for the computation of KPI.i
 * The method computeAverageKPI compute the average of each patient KPI.
 * The abstract method computeKPI compute the KPI for one patient.
 * @author Quentin
 *
 */

public abstract class KeyPerformanceIndicator {
	protected EmergencyDepartment emergencyDepartment;

	public KeyPerformanceIndicator(EmergencyDepartment emergencyDepartment) {
		super();
		this.emergencyDepartment = emergencyDepartment;
	}

	public double computeAverageKPI() {
		ArrayList<Patient> patients = emergencyDepartment.getAllPatients();
		if(patients.size() == 0) {
			return 0;
		}
		else {
			double sumKPI = 0.0;
			int numberPatientsConsidered = 0;
			for (Patient patient : patients) {
				double KPI = computeKPI(patient);
				if(!(Double.isInfinite(KPI))) {
					sumKPI += KPI;
					numberPatientsConsidered++;
				}
			}
			if (numberPatientsConsidered == 0) {
				return 0;
			}
			return sumKPI/numberPatientsConsidered;
		}
	}
	
	public abstract double computeKPI(Patient patient);
	
}
