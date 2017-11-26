package utils;

/**
 * This interface is used to implement the Observable/Observer pattern
 * @author Thomas
 *
 */
public interface Observable {
	
	/**
	 * Adds an observer to the set of observers for this object
	 * @param o is the observer to add
	 */
	void addObserver(Observer o);
	
	/**
	 * Deletes an observer from the set of observers of this object.
	 * @param o is the observer to remove
	 */
	void deleteObserver(Observer o);
	
	/**
	 * Notify all the observers that the object has changed
	 */
	void notifyObservers();
}
