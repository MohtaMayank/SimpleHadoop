package cmu.cs.distsystems.hw3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobStatus {
	public enum JobState {
		PENDING,
		RUNNING,
		SUCCESS,
		FAILED
	}
	
	
	private Job job;
	
	JobState jobState;
	
    Map<Integer, Task> mapTasks;
    Map<Integer, Task> reduceTasks;
	
	public JobStatus(Job job) {
		this.job = job;
        this.jobState = JobState.PENDING;
        
        this.mapTasks = new ConcurrentHashMap<Integer, Task>();
        this.reduceTasks = new ConcurrentHashMap<Integer, Task>();
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public JobState getJobState() {
		return jobState;
	}

	public void setJobState(JobState jobState) {
		this.jobState = jobState;
	}

	public Map<Integer, Task> getMapTasks() {
		return mapTasks;
	}

	public void setMapTasks(Map<Integer, Task> mapTasks) {
		this.mapTasks = mapTasks;
	}

	public Map<Integer, Task> getReduceTasks() {
		return reduceTasks;
	}

	public void setReduceTasks(Map<Integer, Task> reduceTasks) {
		this.reduceTasks = reduceTasks;
	}

}
