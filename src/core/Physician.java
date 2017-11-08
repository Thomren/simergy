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

}
