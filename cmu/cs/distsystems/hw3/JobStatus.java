package cmu.cs.distsystems.hw3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobStatus {

	private Job job;

    boolean mapFinished;
    boolean reduceFinished;
    boolean jobFinished;

    int mapTaskFinished;
    int reduceTaskFinished;
	
	Map<Integer, Task> mapTasks;
	Map<Integer, Task> reduceTasks;
	
	
	public JobStatus(Job job) {
		this.job = job;
        this.mapFinished = false;
        this.reduceFinished = false;
        this.jobFinished = false;
        this.mapTaskFinished = 0;
        this.reduceTaskFinished = 0;
        this.mapTasks = new HashMap<Integer, Task>();
        this.reduceTasks = new HashMap<Integer, Task>();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
