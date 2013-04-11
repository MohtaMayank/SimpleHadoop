package cmu.cs.distsystems.hw3.framework;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cmu.cs.distsystems.hw3.framework.WorkerHeartbeatResponse.Cmd;

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

            if(currentTask.getTaskType() == TaskType.MAP){
                es.submit(new MapWorker(currentTask));
            } else if (currentTask.getTaskType() == TaskType.REDUCE){
                es.submit(new ReduceWorker(currentTask));
            }

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
			TaskRunner runner = new TaskRunner(port);
			System.out.println("Starting task runner on port " + port + "...");
			runner.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
		
	}
	
	public static Class<?> loadClass(String pathToJar, String targetClassName) throws Exception {
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        ClassLoader cl = URLClassLoader.newInstance(urls);

        Class target = null;

        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class clazz = cl.loadClass(className);

            if(className.equals(targetClassName)){
                target = clazz;
            }
        }

        return target;
	}

}


