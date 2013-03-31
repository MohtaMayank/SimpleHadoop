package cmu.cs.distsystems.hw3;

import java.io.Serializable;



public class WorkerHeartbeatResponse implements Serializable {

	Task task;

	public WorkerHeartbeatResponse(Task task) {
		this.task = task;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
