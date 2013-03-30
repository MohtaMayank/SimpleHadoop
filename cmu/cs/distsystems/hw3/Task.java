package cmu.cs.distsystems.hw3;

import java.io.Serializable;

enum TaskState {
	PENDING,
	INPROGRESS,
	SUCCESS,
	RETRY,
	FAILED
}

/**
 * This is the data class representing a specific task in the map-reduce task.
 * An instance of any of its subclass should contain all the information 
 * required to execute this task. For example, MapTask will contain information
 * about which split to work on, the jar file location, the config file location
 * and other such information.
 * @author mayank
 */
abstract public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Job parentJob;
	private int taskId;
	
	private TaskState state;
	
	private int attempts;

	public Task(Job parentJob, int taskId) {
		this.parentJob = parentJob;
		this.taskId = taskId;
		this.state = TaskState.PENDING;
		this.attempts = 0;
	}
	
	
	
	public Job getParentJob() {
		return parentJob;
	}

	public int getTaskId() {
		return taskId;
	}

	public TaskState getState() {
		return state;
	}

	public int getAttempts() {
		return attempts;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
