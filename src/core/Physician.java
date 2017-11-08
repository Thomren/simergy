package core;

import java.util.ArrayList;

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

	public ArrayList<Patient> getTreatedPatients() {
		return treatedPatients;
	}

	public void setTreatedPatients(ArrayList<Patient> treatedPatients) {
		this.treatedPatients = treatedPatients;
	}

	public ArrayList<String> getMessageBox() {
		return messageBox;
	}

	public void setMessageBox(ArrayList<String> messageBox) {
		this.messageBox = messageBox;
	}
	
}
