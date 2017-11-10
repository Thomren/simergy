package core;

/**
 * This interface is used to implement the Observable/Observer pattern
 * @author Thomas
 *
 */
public interface Observer {
	
	/**
	 * This method is called whenever the observed object is changed.
	 * @param arg is the payload sent by the observable  
	 */
	void update(Object arg);
}
