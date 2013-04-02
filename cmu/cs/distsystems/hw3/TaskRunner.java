package cmu.cs.distsystems.hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cmu.cs.distsystems.hw3.WorkerHeartbeatResponse.Cmd;

/**
 * Will be run as a separate JVM. In this JVM, the map or the reduce task will
 * actually run. Will communicate with the respective TaskManager thread to get
 * new tasks.
 * Do we need heartbeat for monitoring if the process is alive??
 * @author mayank
 */

public class TaskRunner {

	private int port;
	private Task currentTask;
	
	public TaskRunner(int port) {
		this.port = port;
	}
	
	public void run() throws Exception {
		while(true) {
			Socket socket = null;
			try {
				socket = new Socket("localhost", port);
				
				WorkerHeartbeat hb = new WorkerHeartbeat(currentTask);
				//Send heart-beat to job tracker.
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject((Object)hb);
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
	
	private void handleResponse(WorkerHeartbeatResponse resp) {
		if(resp.getCommand() == Cmd.IDLE) {
			//Do nothing.
		} else if(resp.getCommand() == Cmd.POLL) {
			//Do nothing
		} else if(resp.getCommand() == Cmd.RUN_NEW_TASK) {
			//TODO:
			//Spawn a new thread and start running the task
			//On any exception, log and fail the complete process.
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
		
	/**
	 * test
	 */
	/*
	public static void main(String[] args) {
		try {
		
			int port = Integer.parseInt(args[0]);
			
			
			
			String jar = "/home/mayank/DistributedSystems/test.jar";
			String mapClassName = "Test";
		
			String javaHome = System.getProperty("java.home");
	        String javaBin = javaHome + File.separator + "bin" + 
	        		File.separator + "java";
	
	        ProcessBuilder builder = new ProcessBuilder(
	                javaBin, "-cp", jar, mapClassName, "Mayank", "Yuchen", "Spaghetti Monster");
	
	        Process process = builder.start();
	        
	        InputStream in = process.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        
	        String line = null;
	        while( (line = br.readLine()) != null ) {
	        	System.out.println(line);
	        }
	        
	        
	        process.waitFor();
	        System.out.println("Exit Value: " + process.exitValue());
        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			TaskRunner runner = new TaskRunner(port);
			runner.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
		
	}

}

class Worker implements Runnable {

	@Override
	public void run() {
		/*	if(task instanceof MapTask) {
		try {
			MapTask mapTask = (MapTask)task;
			
			String jar = mapTask.getParentJob().getJar();
			String mapClassName = mapTask.getParentJob().getMapClass();
			
			String inputFile = mapTask.getMySplit().getFilePartition().getFileName();
			String start = mapTask.getMySplit().getFilePartition().getStart() + "";
			String end = mapTask.getMySplit().getFilePartition().getEnd() + "";
			String tmpDir = null;
			//String tmpDir = mapTask.getParentJob().getTmpDir();
		
			String javaHome = System.getProperty("java.home");
	        String javaBin = javaHome + File.separator + "bin" + 
	        		File.separator + "java";

	        ProcessBuilder builder = new ProcessBuilder(
	                javaBin, "-cp", jar, TASK_RUNNER_CLASS, 
	                mapClassName, inputFile, start, end, tmpDir);

	        Process process = builder.start();
	        
	        
	        
	        process.waitFor();
	        System.out.println("Exit Value: " + process.exitValue());
		} catch (Exception e) {
			//TODO: Handle this properly
			e.printStackTrace();
		}
		}*/

		
	}
	
}
