package resource;

import java.util.ArrayList;

import core.EmergencyDepartment;
import core.Event;
import core.HealthInsurance;
import core.Observable;
import core.Observer;
import core.SeverityLevel;

/**
 * Patient class represents the patients of the emergency department.
 * A patient is an observable which notify its observers of each event happening to him
 * @author Thomas
 *
 */
public class Patient extends Human implements Observable {
	
	protected double arrivalTime;
	protected HealthInsurance healthInsurance;
	protected ArrayList<Event> history;
	protected Room location;
<<<<<<< HEAD
	protected core.severityLevel severityLevel;
=======
	protected core.SeverityLevel severityLevel;
>>>>>>> 0c8ec913202324507e1758625b854dfec6a61ae9
	protected double charges;
	protected ArrayList<Observer> observers;

	public Patient(String name, String surname, double arrivalTime, HealthInsurance healthInsurance, Room location,
<<<<<<< HEAD
			core.severityLevel severityLevel, EmergencyDepartment emergencyDepartment) {
=======
			core.SeverityLevel severityLevel, EmergencyDepartment emergencyDepartment) {
>>>>>>> 0c8ec913202324507e1758625b854dfec6a61ae9
		super(name, surname, "waiting", emergencyDepartment);
		this.arrivalTime = arrivalTime;
		this.healthInsurance = healthInsurance;
		this.location = location;
		this.severityLevel = severityLevel;
		this.history = new ArrayList<Event>();
		this.charges = 0;
		this.observers = new ArrayList<Observer>();
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void deleteObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		if(history.size() > 0) {
			for (Observer observer : observers) {
				observer.update(history.get(history.size() - 1));
			}
		}
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public HealthInsurance getHealthInsurance() {
		return healthInsurance;
	}

	public void setHealthInsurance(HealthInsurance healthInsurance) {
		this.healthInsurance = healthInsurance;
	}

	public ArrayList<Event> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<Event> history) {
		this.history = history;
	}

	public void addEvent(Event event) {
		this.history.add(event);
	}
	
	public Room getLocation() {
		return location;
	}

	public void setLocation(Room location) {
		this.location = location;
	}

	public core.SeverityLevel getSeverityLevel() {
		return severityLevel;
	}

	public void setSeverityLevel(core.SeverityLevel severityLevel) {
		this.severityLevel = severityLevel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}
	
	public void addCharges(double charges) {
		this.charges += charges;
	}

	public ArrayList<Observer> getObservers() {
		return observers;
	}

	public void setObservers(ArrayList<Observer> observers) {
		this.observers = observers;
	}

}