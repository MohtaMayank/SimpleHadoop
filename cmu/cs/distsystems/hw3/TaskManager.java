package cmu.cs.distsystems.hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cmu.cs.distsystems.hw3.WorkerHeartbeatResponse.Cmd;

/**
 * Launch JVM if required. Send the Map or the Reduce Task to the JVM. 
 * This link might be useful:
 * http://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process/723914#723914
 * Handout says that we should reuse the JVM. In that case, i believe we would need some heartbeat
 * kind of mechanism from the child JVM which would indicate to this thread (running in TaskTracker JVM)
 * about the progress and when it has completed the task and ready to accept new task.
 * 
 * In latest hadoop version, each map or reduce task launches a new JVM
 * 
 * @author mayank
 */

public class TaskManager implements Runnable {

	public static final String TASK_RUNNER_CLASS = "TaskRunner";
	public static final int ACCEPT_TIMEOUT = 4000;
	
	private TaskTracker parentTT;
	private int managerId;
	private TaskTracker.WorkerType workerType;
	
	private Task currentTask;
	
	public TaskManager(int managerId, TaskTracker parentTT, 
			TaskTracker.WorkerType type) {
		this.parentTT = parentTT;
		this.managerId = managerId;
		this.workerType = type;
		setCurrentTask(null);
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(5555);
			//If no haertbeat from worker process ... then timeout. 
			serverSocket.setSoTimeout(ACCEPT_TIMEOUT);
			while(true) {
				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
					
					//Should this part be in a separate thread using thread pool or
					//can add retries in RegistryClient as that is the only place through
					//which clients will interact with the RegistryServer.
					ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
					
					WorkerHeartbeat hb = (WorkerHeartbeat)ois.readObject();
					
					WorkerHeartbeatResponse response = handleHeartbeat(hb);
					
					ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
					oos.writeObject(response);
					oos.flush();
					
					ois.close();
					oos.close();
					clientSocket.close();
				} catch (InterruptedIOException timeout) { 
					startTaskRunner();
				} catch (Exception exc) {
					exc.printStackTrace();
				} finally {
					clientSocket.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private WorkerHeartbeatResponse handleHeartbeat(WorkerHeartbeat hb) {
		WorkerHeartbeatResponse response = null;
		
		if(hb.getTaskId() != -1) {
			if(hb.getTask().getPercentComplete() < 100) {
				parentTT.updateTaskStat(hb.getTaskId(), hb.getTask());
				//Indicates that this worker is not free
				setCurrentTask(hb.getTask());
				//The task runner should continue working on the task.
				response = new WorkerHeartbeatResponse(null, WorkerHeartbeatResponse.Cmd.POLL);
			} else {
				parentTT.updateTaskStat(hb.getTaskId(), hb.getTask());
				//Indicates that this worker is free
				setCurrentTask(null);
				//the task runner has completed the task and should start looking for a
				//new task.
				response = new WorkerHeartbeatResponse(null, WorkerHeartbeatResponse.Cmd.IDLE);
			}
		} else {
			if(getCurrentTask() != null && getCurrentTask().getPercentComplete() == 0) {
				response = new WorkerHeartbeatResponse(getCurrentTask(), 
						WorkerHeartbeatResponse.Cmd.RUN_NEW_TASK);
			} else {
				response = new WorkerHeartbeatResponse(null, WorkerHeartbeatResponse.Cmd.IDLE);
			}
		}
		
		return response;
	}

	/**
	 * Kill the old java process if one exists
	 * If there was a task that was being executed mark it as failed.
	 * Create a new Java process. 
	 */
	public void startTaskRunner() {
		
	}

        
	public synchronized Task getCurrentTask() {
		return currentTask;
	}

	public synchronized void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public TaskTracker getParentTT() {
		return parentTT;
	}

	public int getManagerId() {
		return managerId;
	}

	public TaskTracker.WorkerType getWorkerType() {
		return workerType;
	}
}
