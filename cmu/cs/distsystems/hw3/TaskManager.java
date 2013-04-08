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

	//public static final String TASK_RUNNER_CLASS = "cmu.cs.distsystems.hw3.TaskRunnerDummy";
	public static final String TASK_RUNNER_CLASS = "cmu.cs.distsystems.hw3.TaskRunner";
	public static final int ACCEPT_TIMEOUT = 4000;
	
	private TaskTracker parentTT;
	private int managerId;
	private TaskTracker.WorkerType workerType;
	private int port;
	
	private Task currentTask;
	
	private Process workerProcess;
	
	public TaskManager(int managerId, TaskTracker parentTT, 
			TaskTracker.WorkerType type) {
		this.parentTT = parentTT;
		this.managerId = managerId;
		this.workerType = type;
		this.port = parentTT.getPort() + managerId;
		setCurrentTask(null);
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.port);
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
					
					//System.out.println("Received heartbeat from worker");
					
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
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private WorkerHeartbeatResponse handleHeartbeat(WorkerHeartbeat hb) {
		WorkerHeartbeatResponse response;
		
		if(hb.getTask() != null && hb.getTask().getPercentComplete() < 100) {
			parentTT.updateTaskStat(hb.getTaskId(), hb.getTask());
			//Indicates that this worker is not free
			setCurrentTask(hb.getTask());
			//The task runner should continue working on the task.
			response = new WorkerHeartbeatResponse(null, WorkerHeartbeatResponse.Cmd.POLL);
		} else {
			if(hb.getTask() != null && hb.getTask().getPercentComplete() == 100) {
				parentTT.updateTaskStat(hb.getTaskId(), hb.getTask());
				//Indicates to task tracker that worker is free
				setCurrentTask(null);
				response = new WorkerHeartbeatResponse(null, WorkerHeartbeatResponse.Cmd.IDLE);
				System.out.println("Worker finished task " + hb.getTask().getTaskId());

            } else {
				if(getCurrentTask() != null) {
					Task t = getCurrentTask();
					System.out.println("Task Tracker " + parentTT.getId() + " Worker " +
					managerId + " starting task " + t.getTaskId());
					response = new WorkerHeartbeatResponse(t
							, WorkerHeartbeatResponse.Cmd.RUN_NEW_TASK);
				} else {
					response = new WorkerHeartbeatResponse(null, 
							WorkerHeartbeatResponse.Cmd.IDLE);
				}
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
		if(workerProcess != null) {
			workerProcess.destroy();
		}
		try {
			
			String jar = "/Users/mimighostipad/Desktop/DistributedSystems.jar";
			String className = TASK_RUNNER_CLASS;
		
			String javaHome = System.getProperty("java.home");
	        String javaBin = javaHome + File.separator + "bin" + 
	        		File.separator + "java";
	
	        ProcessBuilder builder = new ProcessBuilder(
	                javaBin, "-cp", jar, className, this.port + "");
	
	        workerProcess = builder.start();
	        
	        InputStream in = workerProcess.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        System.out.println(br.readLine());
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
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
