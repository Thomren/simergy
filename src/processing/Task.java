package processing;

public class Task {
	protected double timestamp;
	protected Command command;
	
	public Task(double timestamp, Command command) {
		super();
		this.timestamp = timestamp;
		this.command = command;
	}
	
}
