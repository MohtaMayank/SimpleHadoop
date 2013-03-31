package cmu.cs.distsystems.hw3;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


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

	public enum WorkerType {
		MAPPER,
		REDUCER
	}
	
	private ClusterConfig clusterConfig;
	
	private Map<Integer, TaskManager> managers;

	private ConcurrentLinkedQueue<Integer> freeMappers;
	private ConcurrentLinkedQueue<Integer> freeReducers;
	
	public TaskTracker(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
	}
	
	/**
	 * 1. Register self with Job Tracker and get an Id (first heartbeat)
	 * 2. In the message send information about self like total number of 
	 * mappers and reducer slots available.
	 * 2. Initialize all the task managers
	 */
	public void initialize() {
		//Send First HeartBeat and get unique id
		
		
	}
	
	/**
	 * Send heartbeats to JobTracker with current status and based on
	 * response from the JobTracker, perform required actions
	 */	
	public void run() {
		
	}
	
	/**
	 * Shut down all the worker processes and perform other
	 * required cleanup
	 */
	public void shutdown() {
		
	}
	
	
	public static void main(String[] args) {
		TaskTracker taskTracker = null;
		ClusterConfig clusterConfig;
		try {
			//Read Config to get the location of JobTracker
			clusterConfig = new ClusterConfig(args[0]);
			
			taskTracker = new TaskTracker(clusterConfig);
			
			taskTracker.initialize();
			
			taskTracker.run();
		} finally {
			//If task tracker shuts down, then it should shut down all worker
			//processes along with it
			if(taskTracker != null) {
				taskTracker.shutdown();
			}
		}
		
	}

}
