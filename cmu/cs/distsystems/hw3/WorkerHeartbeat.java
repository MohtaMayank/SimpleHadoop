package cmu.cs.distsystems.hw3;

public class WorkerHeartbeat {

	private Task currentTask;
	
	public WorkerHeartbeat(Task currenTask) {
		this.currentTask = currenTask;
	}

	public int getTaskId() {
		if(currentTask != null) {
			return currentTask.getTaskId();
		} else {
			return -1;
		}
	}
	
	public Task getTask() {
		return this.currentTask;
	}

}
