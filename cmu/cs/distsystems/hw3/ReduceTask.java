package cmu.cs.distsystems.hw3;

import java.util.List;

public class ReduceTask extends Task {
	private static final long serialVersionUID = 1L;
	
	int partitionNumber;
	List<String> mapOpLocations;

	public ReduceTask(Job parentJob, int taskId) {
		super(parentJob, taskId);
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
