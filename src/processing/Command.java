package processing;

/**
 * Command interface used for command pattern
 * @author Thomas
 *
 */
@FunctionalInterface
public interface Command {
	
	/**
	 * This method execute the stored command
	 */
	public void execute();
}