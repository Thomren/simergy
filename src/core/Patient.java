package core;

import java.util.ArrayList;

/**
 * Patient class represents the patients of the emergency department.
 * A patient is an observable which notify its observers of each event happening to him
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

}
