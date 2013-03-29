package cmu.cs.distsystems.hw3;

/**
 * This process manages tasks on th local machine. It periodically sends heartbeats
 * to the JobTracker,  which in return message sends its tasks to run.
 * The Map and reduce tasks should be run in a separate JVM so that the failure 
 * of any particular task does not bring down the TaskTracker.
 * The TaskRunner thread takes care of launching and monitoring the 
 * the JVM  
 * @author mayank
 */

public class TaskTracker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
