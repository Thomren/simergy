package core;

/**
 * This interface is used to implement the Observable/Observer pattern
 * @author Thomas
 *
 */
public interface Observable {
	void addObserver(Observer o);
	
	void deleteObserver(Observer o);
	
	void notifyObservers();
}
