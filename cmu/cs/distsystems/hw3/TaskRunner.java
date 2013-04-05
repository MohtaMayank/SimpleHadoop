package cmu.cs.distsystems.hw3;

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
	private ExecutorService es;
	
	public TaskRunner(int port) {
		this.port = port;
		es = Executors.newFixedThreadPool(1);
	}
	
	public void run() throws Exception {
		while(true) {
			Socket socket = null;
			try {
				socket = new Socket("localhost", port);
				
				
				
				WorkerHeartbeat hb = new WorkerHeartbeat(createSnapshot());
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
	
	private Task createSnapshot() {
		//Return the status of the current task which is executing
		Task snapshot;
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
			//Do nothing.
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
	private Task task;
	
	public Worker(Task task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		if(task instanceof MapTask) {
			try {
				
				MapTask mapTask = (MapTask)task;
				
				String jar = mapTask.getParentJob().getJar();
				String mapClassName = mapTask.getParentJob().getMapClass();
				
				Class<?> mapClazz = loadClass(jar, mapClassName);
				Constructor<?> constructor = mapClazz.getConstructor();
                Mapper mapper = (Mapper) constructor.newInstance(mapTask);
                mapper.run();

				

		        
		        
			} catch (Exception e) {
				//TODO: Handle this properly
				e.printStackTrace();
			}
		}

		
	}
	
	public Class<?> loadClass(String dir, String className) throws Exception {
		URL[] urls = new URL[]{new URL(dir)};
		URLClassLoader loader = new URLClassLoader(urls);
		return loader.loadClass(className);
	}
	
}
