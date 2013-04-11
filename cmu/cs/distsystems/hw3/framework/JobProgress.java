package cmu.cs.distsystems.hw3.framework;

import java.io.Serializable;

public class JobProgress implements Serializable {

	int jobId;
	
    double percentMapTaskFinished;
    double percentReduceTaskFinished;
    
    JobStatus.JobState state;

	public JobProgress(int id) {
		this.jobId = id;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public JobStatus.JobState getState() {
		return state;
	}

	public void setState(JobStatus.JobState state) {
		this.state = state;
	}

	public double getPercentMapTaskFinished() {
		return percentMapTaskFinished;
	}

	public void setPercentMapTaskFinished(double percentMapTaskFinished) {
		this.percentMapTaskFinished = percentMapTaskFinished;
	}

	public double getPercentReduceTaskFinished() {
		return percentReduceTaskFinished;
	}

	public void setPercentReduceTaskFinished(double percentReduceTaskFinished) {
		this.percentReduceTaskFinished = percentReduceTaskFinished;
	}
}
