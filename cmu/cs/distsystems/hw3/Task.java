package cmu.cs.distsystems.hw3;

import java.io.Serializable;

enum TaskState {
	PENDING,
	RUNNING,
	SUCCESS,
	RETRY,
	FAILED
}

enum TaskType {
	MAP,
	REDUCE
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
	private TaskType taskType;
	
	private double percentComplete;
	private long startTime;
	private long endTime;
	private int attemptNum;
	
	private String taskTrackerId;

	public Task(Job parentJob, int taskId, TaskType type) {
		this.parentJob = parentJob;
		this.taskId = taskId;
		this.taskType = type;
		
		this.state = TaskState.PENDING;
		this.percentComplete = 0;
		this.attemptNum = 0;
	}
	
	//Copy constructor
	public Task(Task task) {
		this.parentJob = task.getParentJob();
		this.taskId = task.getTaskId();
		this.state = task.getState();
		this.attemptNum = task.getAttemptNum();
		this.percentComplete = task.getPercentComplete();
		this.startTime = task.getStartTime();
		this.endTime = task.getEndTime();
		this.taskType = task.getTaskType();
	}
	
	
	public Job getParentJob() {
		return parentJob;
	}

	public int getTaskId() {
		return taskId;
	}

	public synchronized TaskState getState() {
		return state;
	}
	
	public synchronized void setTaskState(TaskState state) {
		this.state = state;
	}
	
	public TaskType getTaskType() {
		return this.taskType;
	}
	
	public synchronized double getPercentComplete() {
		return percentComplete;
	}

	public synchronized void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}

	public synchronized long getStartTime() {
		return startTime;
	}

	public synchronized void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public synchronized long getEndTime() {
		return endTime;
	}

	public synchronized void setEndTime(long endTime) {
		this.endTime = endTime;
	}

    public synchronized void setState(TaskState state){
        this.state = state;
    }

	public synchronized int getAttemptNum() {
		return attemptNum;
	}

	public synchronized void setAttemptNum(int attemptNum) {
		this.attemptNum = attemptNum;
	}
	
	public String getTaskTrackerId() {
		return taskTrackerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (taskId != other.taskId)
			return false;
		return true;
	}
	
}
