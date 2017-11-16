package processing;

/**
 * Command interface used for command pattern
 * @author Thomas
 *
 */
@FunctionalInterface
public interface Command {
   public void execute();
}