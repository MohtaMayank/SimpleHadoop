package cmu.cs.distsystems.hw3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * JobTracker starts and maintains the MapReduce Job.
 * JobTracker runs on master node and is responsible for scheduling tasks to different workers
 * It also has other responsibilities like monitoring, failure recovery etc. 
 * @author mayank
 */

public class JobTracker {

	
	/**
	 * 
	 */

    String host;
    int port;
    static int nextJobId = 0;
    static int nextTaskId = 0;
    Queue<Task> pendingTasks = new ConcurrentLinkedQueue<Task>();
    Map<Integer,JobStatus> status = new ConcurrentHashMap<Integer, JobStatus>();


    public static synchronized int getNewJobId(){
        nextJobId++;
        return nextJobId;
    }

    public static synchronized int getNextTaskId(){
        nextTaskId++;
        return nextTaskId;
    }




	public void run() {

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
