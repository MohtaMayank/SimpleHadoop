package cmu.cs.distsystems.hw3;

public class WorkerHeartbeat {

	private boolean isRunningTask;
	private int taskId;
	private double percentComplete;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public boolean isRunningTask() {
		return isRunningTask;
	}

	public int getTaskId() {
		return taskId;
	}

	public double getPercentComplete() {
		return percentComplete;
	}

}
