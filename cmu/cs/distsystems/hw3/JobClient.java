package cmu.cs.distsystems.hw3;

import java.util.List;


/**
 * This is the client which the map-reduce application calls to submit a job.
 *  
 * @author mayank
 */

public class JobClient {

	
	public static void submitJob(Job job) {
		//Ask Job Tracker for a new job Id.
		
		//Check output specification for the job. Error if output dir already exists.
		
		//Compute input splits for the job.
		
		//Copy the resources needed to run the job. (Jar file, config file, computed splits)
		//Might not be needed if we use AFS.
		
		//Make an RPC call to JobTracker to start the job.
		
	}
	
	//TODO: Implement this!
	private static List<Split> computeSplits() {
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
