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
	
	public TaskManager(int managerId, TaskTracker parentTT, 
			TaskTracker.WorkerType type) {
		this.parentTT = parentTT;
		this.managerId = managerId;
		this.workerType = workerType;
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(5555);
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
		
		if(hb.isRunningTask()) {
			response = new WorkerHeartbeatResponse(null);
		} else {
			//Set the shared variable that I am free.
			
			//task = parentTT.getTask()
			/*if(task != null) {
				
			} else {
			
			}*/
				
		}
		
		return null;
	}

	public void startTaskRunner() {
		
	}

        
	/**
	 * test
	 */
	public static void main(String[] args) {
		try {
		
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

	}

}
