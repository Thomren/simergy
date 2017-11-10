package resource;

import java.util.ArrayList;

import core.Observer;

/**
 * The Physician class represents a physician of the Emergency Department
 * @author Thomas
 *
 */
public class Physician extends Human implements Observer {

	protected String username;
	protected String state;
	protected ArrayList<Patient> overseenPatients;
	protected ArrayList<Patient> treatedPatients;
	protected ArrayList<String> messageBox;
	
	@Override
	public void update(Object message) {
		messageBox.add((String) message);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ArrayList<Patient> getOverseenPatients() {
		return overseenPatients;
	}

	public void setOverseenPatients(ArrayList<Patient> overseenPatients) {
		this.overseenPatients = overseenPatients;
	}
	
	/**
	 * This method add a patient to the overseen patients' list of the physician and subscribe the physician
	 * to the patient's events diffusion.
	 * @param patient is the patient to add the list of the physician's currently followed patients
	 */
	public void addOverseenPatien(Patient patient) {
		patient.addObserver(this);
		overseenPatients.add(patient);
	}
	
	/**
	 * This method remove a patient from the overseen patients' list of the physician and unsubscribe the
	 * physician from the patient's events diffusion
	 * @param patient is the patient to remove from the list of the physician's currently followed patients
	 */
	public void removeOverseenPatient(Patient patient) {
		patient.deleteObserver(this);
		overseenPatients.remove(patient);
	}

	public ArrayList<Patient> getTreatedPatients() {
		return treatedPatients;
	}

	public void setTreatedPatients(ArrayList<Patient> treatedPatients) {
		this.treatedPatients = treatedPatients;
	}
	
	/**
	 * This method add a patient to the treated patients' list of the physician.
	 * @param patient is the patient to add the list of the physician's treated patients
	 */
	public void addTreatedPatient(Patient patient) {
		treatedPatients.add(patient);
	}
	
	/**
	 * This method remove a patient from the treated patients' list of the physician.
	 * @param patient is the patient to remove from the list of the physician's treated patients
	 */
	public void removeTreatedPatient(Patient patient) {
		treatedPatients.remove(patient);
	}

	public ArrayList<String> getMessageBox() {
		return messageBox;
	}

	public void setMessageBox(ArrayList<String> messageBox) {
		this.messageBox = messageBox;
	}
	
}
