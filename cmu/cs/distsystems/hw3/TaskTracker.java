package cmu.cs.distsystems.hw3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This process manages tasks on the local machine. It periodically sends heartbeats
 * to the JobTracker,  which in return message sends its tasks to run.
 * The Map and reduce tasks should be run in a separate JVM so that the failure 
 * of any particular task does not bring down the TaskTracker.
 * The TaskRunner thread takes care of launching and monitoring the 
 * the JVM  
 * @author mayank
 */

public class TaskTracker {

	public enum WorkerType {
		MAPPER,
		REDUCER
	}
	
	private String id;
	
	private ClusterConfig clusterConfig;
	
	private int taskMgrCounter;
	
	private Map<Integer, TaskManager> managers;

	private ConcurrentHashMap<Integer, Task> runningTaskStats;
	
	public TaskTracker(int port, ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		this.taskMgrCounter = 0;
		try {
			this.id = InetAddress.getLocalHost().getHostName() + ":" + port;
		} catch (Exception e) {
			System.out.println("Cannot get task tracker hostname ... Exitting");
			e.printStackTrace();
			System.exit(2);
		}
		WorkerConfig workerConfig = clusterConfig.getWorkerConfig(id);
		
		//Initialize the Mappers
		for(int i = 0; i < workerConfig.getNumMapSlots(); i++) {
			int id = this.taskMgrCounter++;
			TaskManager mgr = new TaskManager(id, this, WorkerType.MAPPER);
			this.managers.put(id, mgr);
		}
		
		//Initialize the Reducers
		for(int i = 0; i < workerConfig.getNumReduceSlots(); i++) {
			int id = this.taskMgrCounter++;
			TaskManager mgr = new TaskManager(id, this, WorkerType.REDUCER);
			this.managers.put(id, mgr);
		}
		
	}
	
	/**
	 * 1. Register self with Job Tracker and get an Id (first heartbeat)
	 * 2. In the message send information about self like total number of 
	 * mappers and reducer slots available.
	 * 2. Initialize and start all the task managers
	 */
	public void initialize() throws Exception {
		Socket socket = null;
		try {
			socket = new Socket(clusterConfig.getJobTrackerHost(), 
					clusterConfig.getJobTrackerPort());
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			TaskTrackerHB hb = new TaskTrackerHB(id, getFreeMapperSlots(), getFreeReducerSlots(), true, null);
			oos.writeObject((Object)hb);
			oos.flush();
			
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	        TaskTrackerHBResponse resp = (TaskTrackerHBResponse)ois.readObject();
	        
	        if(resp.getCommand() == TaskTrackerHBResponse.Cmd.INIT) {
	        	System.out.println("Task Tracker " + id + " successfully registered with Job Tracker");
	        }
	
			oos.close();
			ois.close();
		
		} finally {
			if(socket != null){
				socket.close();
			}
		}
		
	}
	
	private int getFreeReducerSlots() {
		int freeMapSlots = 0;
		for(TaskManager mgr : managers.values()) {
			if(mgr.getWorkerType() == WorkerType.MAPPER && mgr.getCurrentTask() == null) {
				freeMapSlots++;
			}
		}
		return freeMapSlots;
	}

	private int getFreeMapperSlots() {
		int freeReduceSlots = 0;
		for(TaskManager mgr : managers.values()) {
			if(mgr.getWorkerType() == WorkerType.MAPPER && mgr.getCurrentTask() == null) {
				freeReduceSlots++;
			}
		}
		return freeReduceSlots;
	}

	private void handleResponse(TaskTrackerHBResponse resp) {
		
		if(resp.getCommand() == TaskTrackerHBResponse.Cmd.SHUT_DOWN) {
			shutdown();
		} else if(resp.getCommand() == TaskTrackerHBResponse.Cmd.NEW_TASK) {
			Task newTask = resp.getNewTask();
			TaskManager freeWorker;
			//TODO: Will this work or do we need a taskType variable?
			if(newTask instanceof MapTask) {
				
				
			} else if (newTask instanceof ReduceTask) {
				
			} else {
				//Cannot happen!
				System.out.println("Invalid task type ... Ignoring");
			}
			
		} else if(resp.getCommand() == TaskTrackerHBResponse.Cmd.NEW_TASK) {
			
		}
		
	}

	/**
	 * Send heartbeats to JobTracker with current status and based on
	 * response from the JobTracker, perform required actions
	 */	
	public void run() throws Exception {
		while(true) {
			Socket socket = null;
			try {
				socket = new Socket(clusterConfig.getJobTrackerHost(), 
						clusterConfig.getJobTrackerPort());
				
				//Send heart-beat to job tracker.
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
				List<Task> taskSnapshot = createTaskSnapshot();
				TaskTrackerHB hb = new TaskTrackerHB(id, getFreeMapperSlots(), getFreeReducerSlots(), 
						false, taskSnapshot);
				oos.writeObject((Object)hb);
				oos.flush();
				
				//Receive response from job tracker.
		        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		        TaskTrackerHBResponse resp = (TaskTrackerHBResponse)ois.readObject();
		        
		        //Handle the response.
		        handleResponse(resp);
		
				oos.close();
				ois.close();
			
			} finally {
				if(socket != null){
					socket.close();
				}
			}
		}
		
	}
	
	private List<Task> createTaskSnapshot() {
		// TODO Auto-generated method stub
		
		return null;
	}

	/**
	 * Shut down all the worker processes and perform other
	 * required cleanup
	 */
	public void shutdown() {
		
	}
	
	public void updateTaskStat(int taskId, Task task) {
		this.runningTaskStats.put(taskId, task);
	}
	
	
	public static void main(String[] args) {
		TaskTracker taskTracker = null;
		ClusterConfig clusterConfig;
		//Read Config to get the location of JobTracker
		int port = Integer.parseInt(args[0]);
		clusterConfig = new ClusterConfig(args[1]);
		
		taskTracker = new TaskTracker(port, clusterConfig);
		
		try {
			taskTracker.initialize();
			
			taskTracker.run();
		} catch (Exception e) { 
			System.out.println("Task Tracker " + taskTracker.id + " failed!");
			e.printStackTrace();
		} finally {
			//If task tracker shuts down, then it should shut down all worker
			//processes along with it
			if(taskTracker != null) {
				taskTracker.shutdown();
			}
		}
		
	}

}
