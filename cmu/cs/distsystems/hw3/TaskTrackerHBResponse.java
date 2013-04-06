package cmu.cs.distsystems.hw3;

import java.io.Serializable;


public class TaskTrackerHBResponse implements Serializable {	
	public enum Cmd {
		INIT,	//In response to task trackers message with id -1. Ask TT to initialize
		NEW_TASK,	//If there is a new task
		POLL,	//If there is nothing to say
		RESTART, //If the Job Tracker wants this task tracker to restart.
		SHUT_DOWN	//If Job tracker wants this task tracker to shut down
	}
	
	Cmd command;
	Task newTask;
	
	/**
	 * 
	 * @param task the new task for the task tracker. 
	 * If this is null, then no new task is started
	 * @param command NEW_TASK: Start new task
	 * SHUT_DOWN: shut down the task tracker and associated workers.
	 * RESTART: restart the task tracker. (If network  failure, 
	 * job tracker might assume this as failed. On recovery it can ask for a 
	 * restart and reinitialize)
	 */
	public TaskTrackerHBResponse(Task task, Cmd command) {
		this.newTask = task;
		this.command = command;
	}
	
	public Cmd getCommand() {
		return command;
	}

	public Task getNewTask() {
		return newTask;
	}
		
}