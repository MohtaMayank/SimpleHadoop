package cmu.cs.distsystems.hw3.framework;

public class ReduceTask extends Task {
	private static final long serialVersionUID = 1L;

	//This is the partitionNumber for the reducce task. It is also
	//like an id since it is unique.
	int partitionNumber;

	public ReduceTask(Job parentJob, int taskId, int partitionNumber) {
		super(parentJob, taskId, TaskType.REDUCE);
		this.partitionNumber = partitionNumber;
	}
	
	//Copy Constructor
	public ReduceTask(ReduceTask task) {
		super(task);
		this.partitionNumber = task.getPartitionNumber();
	}
	
	public int getPartitionNumber() {
		return partitionNumber;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
