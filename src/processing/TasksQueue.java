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
	 * Retrieve and execute the first task in the queue (smaller timestamp). Return the timestamp of the executed task.
	 * @return double representing the time of the next task.
	 */
	public Task executeNextTask() {
		Task task = queue.poll();
		task.getCommand().execute();
		return task;
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

	@Override
	public String toString() {
		StringBuffer content = new StringBuffer();
		content.append("Tasks queue : \n");
		for (Task task : queue) {
			content.append(task).append('\n');
		}
		return content.toString();
	}
	
}
