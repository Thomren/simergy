package utils;

import java.util.ArrayList;

import core.EmergencyDepartment;
import resources.Patient;

/**
 * This is an abstract class for the computation of KPI.i
 * The method computeAverageKPI computes the average of each patient KPI.
 * The abstract method computeKPI computes the KPI for one patient.
 * @author Quentin
 *
 */

public abstract class KeyPerformanceIndicator {
	protected EmergencyDepartment emergencyDepartment;

	public KeyPerformanceIndicator(EmergencyDepartment emergencyDepartment) {
		super();
		this.emergencyDepartment = emergencyDepartment;
	}

	/**
	 * This method computes the average of each patient KPI.
	 * @return the average computed KPI for all the patients in the ED
	 */
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
	
	/**
	 * This method computes the KPI for one patient.
	 * @param patient
	 * @return the computed KPI
	 */
	public abstract double computeKPI(Patient patient);
	
}
