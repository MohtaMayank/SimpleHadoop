package cmu.cs.distsystems.hw3;

import java.io.Serializable;



public class WorkerHeartbeatResponse implements Serializable {
	public enum Cmd {
		RUN_NEW_TASK,	//Command to run new task.
		POLL,	//Dummy response - saying "keep up the good work"
		IDLE,	//Response that says "no work yet".
		SHUTDOWN	//Command to shutdown the worker process
	}
	
	Task newTask;
	Cmd command;

	public WorkerHeartbeatResponse(Task task, Cmd command) {
		this.newTask = task;
		this.command = command;
	}
	
	public Task getNewTask() {
		return newTask;
	}

	public Cmd getCommand() {
		return command;
	}	

}
