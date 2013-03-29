package cmu.cs.distsystems.hw3;

import java.util.Map;

public class JobStat {

	private Job job;
	
	long startTime;
	long endTime;
	
	Map<Integer, Task> mapTasks;
	Map<Integer, Task> reduceTasks;
	
	
	public JobStat(Job job) {
		this.job = job;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
