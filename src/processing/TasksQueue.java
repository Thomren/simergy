package processing;

import java.util.PriorityQueue;

/**
 * Invoker class of the command pattern. It stores the task to execute with their timestamp and
 * can execute them in the right order.
 * @author Thomas
 *
 */
public class TasksQueue {
	protected PriorityQueue<Task> queue;

	public TasksQueue() {
		super();
		queue = new PriorityQueue<Task>(11, (task1, task2) -> Double.compare(task1.timestamp,task2.timestamp));
	}
	
	/**
	 * Retrieve and execute the first task in the queue (smaller timestamp)
	 */
	public void executeNextTask() {
		queue.poll().command.execute();
	}
	
	public Task getNextTask() {
		return queue.peek();
	}
	
	public void addTask(Task task) {
		queue.offer(task);
	}
	
	public void removeTask(Task task) {
		queue.remove(task);
	}

	public PriorityQueue<Task> getQueue() {
		return queue;
	}
	
	
	
}
