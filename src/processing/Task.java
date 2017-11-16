package processing;

public class Task {
	protected double timestamp;
	protected Command command;
	
	public Task(double timestamp, Command command) {
		super();
		this.timestamp = timestamp;
		this.command = command;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	
	
}
