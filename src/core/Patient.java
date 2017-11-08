package core;

import java.util.ArrayList;

/**
 * Patient class represents the patients of the emergency department.
 * A patient is an observable which notify its observers of each event happening to him
 * @author Thomas
 *
 */
public class Patient extends Human implements Observable {
	
	protected double arrivalTime;
	protected HealthInsurance healthInsurance;
	protected ArrayList<String> history;
	protected Room location;
	protected severityLevel severityLevel;
	protected String state;
	protected double charges;
	protected ArrayList<Observer> observers;

	public Patient(String name, String surname, double arrivalTime, HealthInsurance healthInsurance, Room location,
			core.severityLevel severityLevel) {
		super(name, surname);
		this.arrivalTime = arrivalTime;
		this.healthInsurance = healthInsurance;
		this.location = location;
		this.severityLevel = severityLevel;
		this.history = new ArrayList<String>();
		this.state = "waiting";
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

	public ArrayList<String> getHistory() {
		return history;
	}

	public void setHistory(ArrayList<String> history) {
		this.history = history;
	}

	public Room getLocation() {
		return location;
	}

	public void setLocation(Room location) {
		this.location = location;
	}

	public severityLevel getSeverityLevel() {
		return severityLevel;
	}

	public void setSeverityLevel(severityLevel severityLevel) {
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

	public ArrayList<Observer> getObservers() {
		return observers;
	}

	public void setObservers(ArrayList<Observer> observers) {
		this.observers = observers;
	}

}
