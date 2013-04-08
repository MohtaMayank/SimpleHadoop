package cmu.cs.distsystems.hw3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobStatus {

    public enum JobState {
		PENDING,
		MAP_RUNNING,
        MAP_FINISHED,
        REDUCE_RUNNING,
		SUCCESS,
		FAILED
	}
	
	
	private Job job;
	
	JobState jobState;
	
    Map<Integer, Task> mapTasks;
    Map<Integer, Task> reduceTasks;
    private int numTotalMapTask;
    private int numTotalReduceTask;
    private int numFinishedMapTask;
    private int numFinishedReduceTask;
	
	public JobStatus(Job job) {
		this.job = job;
        this.jobState = JobState.PENDING;
        
        this.mapTasks = new ConcurrentHashMap<Integer, Task>();
        this.reduceTasks = new ConcurrentHashMap<Integer, Task>();
        this.numTotalMapTask = job.getNumSplits();
        this.numTotalReduceTask = job.getNumReducers();
        this.numFinishedMapTask = 0;
        this.numFinishedReduceTask = 0;
	}

    public void updateStatus(){

        System.out.println("current state:"+jobState);

        if(jobState == JobState.MAP_RUNNING || jobState == JobState.REDUCE_RUNNING){

            int numTaskComplete = 0;
            Map<Integer,Task> runningTasks = null;

            if (jobState == JobState.MAP_RUNNING){
                runningTasks = this.mapTasks;
            } else if(jobState == JobState.REDUCE_RUNNING){
                runningTasks = this.reduceTasks;
            }

            for(int taskId:runningTasks.keySet()){
                Task task = runningTasks.get(taskId);
                if(task.getState() == TaskState.SUCCESS){
                    numTaskComplete += 1;
                }
            }

            if(jobState == JobState.MAP_RUNNING){
                this.numFinishedMapTask = numTaskComplete;
                if(numFinishedMapTask == numTotalMapTask){
                    setJobState(JobState.MAP_FINISHED);
                }
            } else {
                this.numFinishedReduceTask = numTaskComplete;
                if(numFinishedReduceTask == numTotalReduceTask){
                    setJobState(JobState.SUCCESS);
                }
            }

            System.out.println("finished map tasks:" + this.numFinishedMapTask);
            System.out.println("map task remains:"+ (this.numTotalMapTask - this.numFinishedMapTask));

        }
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

	public synchronized void setJobState(JobState jobState) {
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
