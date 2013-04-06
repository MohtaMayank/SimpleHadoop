/*package cmu.cs.distsystems.hw3;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cmu.cs.distsystems.hw2.HelloGiver;
import cmu.cs.distsystems.hw3.WorkerHeartbeatResponse.Cmd;

*//**
 * Will be run as a separate JVM. In this JVM, the map or the reduce task will
 * actually run. Will communicate with the respective TaskManager thread to get
 * new tasks.
 * Do we need heartbeat for monitoring if the process is alive??
 * @author mayank
 *//*

public class TaskRunnerDummy {

	private int port;
	private Task currentTask;
	private ExecutorService es;
	
	public TaskRunnerDummy(int port) {
		this.port = port;
		es = Executors.newFixedThreadPool(1);
		this.currentTask = null;
	}
	
	public void run() throws Exception {
		while(true) {
			Thread.sleep(500);
			Socket socket = null;
			try {
				socket = new Socket("localhost", port);
				
				WorkerHeartbeat hb = new WorkerHeartbeat(createSnapshot());
				//Send heart-beat to job tracker.
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(hb);
				oos.flush();
				
				//Receive response from job tracker.
		        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		        WorkerHeartbeatResponse resp = (WorkerHeartbeatResponse)ois.readObject();
		        
		        //Handle the response.
		        handleResponse(resp);
		
				oos.close();
				ois.close();
			
			} catch (Exception e) { 
				e.printStackTrace();
				System.exit(2);
			} finally {
				if(socket != null){
					socket.close();
				}
			}
		}
	}
	
	private Task createSnapshot() {
		//Return the status of the current task which is executing
		Task snapshot;
		if(currentTask == null) {
			return null;
		}
		
		if(currentTask instanceof MapTask) {
			snapshot = new MapTask((MapTask)currentTask);
		} else if(currentTask instanceof ReduceTask) {
			snapshot = new ReduceTask((ReduceTask)currentTask);
		} else {
			snapshot = null;
		}
		return snapshot;
	}

	private void handleResponse(WorkerHeartbeatResponse resp) {
		if(resp.getCommand() == Cmd.IDLE) {
			currentTask = null;
		} else if(resp.getCommand() == Cmd.POLL) {
			//Do nothing
		} else if(resp.getCommand() == Cmd.RUN_NEW_TASK) {
			//TODO:
			//Spawn a new thread and start running the task
			//On any exception, log and fail the complete process.
			setCurrentTask(resp.getNewTask());
			es.submit(new Worker(currentTask));
		} else if(resp.getCommand() == Cmd.SHUTDOWN) {
			System.exit(0);
		}
	}
	
	public synchronized void setCurrentTask(Task task) {
		this.currentTask = task;
	}
	
	public synchronized Task getCurrentTask() {
		return currentTask;
	}
	
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			System.out.println("Starting worker at port " + port + "...");
			TaskRunnerDummy runner = new TaskRunnerDummy(port);
			runner.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
		
	}

}

class Worker implements Runnable {
	private Task task;
	
	public Worker(Task task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		try {
			if(task instanceof MapTask) {
				System.out.println("Received map task split: " + ((MapTask)task).getMySplit());
				Thread.sleep(1500);
				task.setPercentComplete(100);
			} else if(task instanceof ReduceTask) {
				
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
*/