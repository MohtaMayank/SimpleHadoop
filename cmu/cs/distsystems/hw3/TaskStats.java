package cmu.cs.distsystems.hw3;

import java.io.Serializable;


/**
 * This class contains all the statistics about the tasks
 * @author mayank
 *
 */
public class TaskStats implements Serializable {

	private double percentComplete;
	private long startTime;
	private long endTime;
	private int attemptNum;	
	
	public TaskStats() {
		this.percentComplete = 0;
		this.attemptNum = 0;
	}

	public synchronized double getPercentComplete() {
		return percentComplete;
	}

	public synchronized void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getAttemptNum() {
		return attemptNum;
	}

	public void setAttemptNum(int attemptNum) {
		this.attemptNum = attemptNum;
	}

}
