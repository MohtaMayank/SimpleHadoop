package cmu.cs.distsystems.hw3;

/**
 * Contains all the information that is required for completing
 * a map task.
 * Things required are:
 * 1. Jar file location (available in parentJob)
 * 2. Map Class name. (available in parentJob)
 * 3. Input data to process (Split which contains all the file partitions)
 * 4. Output directory location (from parentJob and taskId)
 * 5. Map Input key value and output key value (available in parentJob)
 * 6. Combiner class (available in parentJob). 
 * @author mayank
 */

public class MapTask extends Task {
	
	private static final long serialVersionUID = 1L;
	
	private Split mySplit;
	
	public MapTask(Job parentJob, int taskId, Split split) {
		super(parentJob, taskId, TaskType.MAP);
		
		this.mySplit = split;
	}
	
	//Copy constructor
	public MapTask(MapTask task) {
		super(task);
		this.mySplit = task.getMySplit();
	}

	public Split getMySplit() {
		return mySplit;
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
