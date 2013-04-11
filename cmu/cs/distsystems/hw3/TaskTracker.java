package cmu.cs.distsystems.hw3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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

	public static final long JOB_TRACKER_HB_TIME = 500;
	
	private String id;
	private int port;
	
	private ClusterConfig clusterConfig;
	
	private int taskMgrCounter;
	
	private Map<Integer, TaskManager> managers;

	private ConcurrentHashMap<Integer, Task> runningTaskStats;
	
	public TaskTracker(int port, ClusterConfig clusterConfig) {
		this.port = port;
		this.clusterConfig = clusterConfig;
		this.taskMgrCounter = 0;
		
		managers = new HashMap<Integer, TaskManager>();
		runningTaskStats = new ConcurrentHashMap<Integer, Task>();
		
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
	
	public void startAllWorkers() {
		for(TaskManager mgr : managers.values()) {
			Thread t = new Thread(mgr);
			t.start();
		}
	}
	
	private int getFreeMapperSlots() {
		int freeMapSlots = 0;
		for(TaskManager mgr : managers.values()) {
			if(mgr.getWorkerType() == WorkerType.MAPPER && mgr.getCurrentTask() == null) {
				freeMapSlots++;
			}
		}
		return freeMapSlots;
	}
	
	private TaskManager getFreeWorker(WorkerType type) {
		for(TaskManager mgr : managers.values()) {
			if(mgr.getWorkerType() == type && mgr.getCurrentTask() == null) {
				return mgr;
			}
		}
		return null;
	}

	private int getFreeReducerSlots() {
		int freeReduceSlots = 0;
		for(TaskManager mgr : managers.values()) {
			if(mgr.getWorkerType() == WorkerType.REDUCER && mgr.getCurrentTask() == null) {
				freeReduceSlots++;
			}
		}
		return freeReduceSlots;
	}

	
	private void handleResponse(TaskTrackerHBResponse resp) {
		
		if(resp.getCommand() == TaskTrackerHBResponse.Cmd.SHUT_DOWN) {
			shutdown();
		} else if(resp.getCommand() == TaskTrackerHBResponse.Cmd.NEW_TASKS) {
			List<Task> newTasks = resp.getNewTasks();
			TaskManager freeWorker;

			for(Task newTask : newTasks ) {
				if(newTask.getTaskType() == TaskType.MAP) {
					freeWorker = getFreeWorker(WorkerType.MAPPER);
					if(freeWorker != null) {
						freeWorker.setCurrentTask(newTask);
						updateTaskStat(newTask.getTaskId(), newTask);
					}
				} else if (newTask.getTaskType() == TaskType.REDUCE) {
					freeWorker = getFreeWorker(WorkerType.REDUCER);
					if(freeWorker != null) {
						freeWorker.setCurrentTask(newTask);
						updateTaskStat(newTask.getTaskId(), newTask);
					}
				} else {
					//Cannot happen!
					System.out.println("Invalid task type ... Ignoring");
				}
			}
		} else if(resp.getCommand() == TaskTrackerHBResponse.Cmd.POLL) {
			//Do nothing
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
						clusterConfig.getJobTrackerWorkerCommPort());
				
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
			
			//Sleep for 1 second
			Thread.sleep(JOB_TRACKER_HB_TIME);
		}
		
	}
	
	/**
	 * Create a snapshot of the current running tasks.
	 * @return
	 */
	private List<Task> createTaskSnapshot() {
		List<Task> taskSnapshot = new ArrayList<Task>();
		List<Task> finishedTasks = new ArrayList<Task>();
		
		for(Task task : runningTaskStats.values()) {
			if(task instanceof MapTask) {
				taskSnapshot.add(new MapTask((MapTask)task));
			} else if(task instanceof ReduceTask) {
				taskSnapshot.add(new ReduceTask((ReduceTask)task));
			}
			
			if(task.getPercentComplete() == 100) {
				finishedTasks.add(task);
			}
			
		}
		
		//cleanup tasks which have finished.
		for(Task task : finishedTasks) {
			runningTaskStats.remove(task.getTaskId());
		}
		
		return taskSnapshot;
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
	
	public String getId() {
		return id;
	}

	public int getPort() {
		return port;
	}

	public ClusterConfig getClusterConfig() {
		return clusterConfig;
	}

	public int getTaskMgrCounter() {
		return taskMgrCounter;
	}

	public Map<Integer, TaskManager> getManagers() {
		return managers;
	}

	public ConcurrentHashMap<Integer, Task> getRunningTaskStats() {
		return runningTaskStats;
	}

	
	
	public static void main(String[] args) {
		TaskTracker taskTracker = null;
		ClusterConfig clusterConfig;
		//Read Config to get the location of JobTracker
		int port = Integer.parseInt(args[0]);
		clusterConfig = new ClusterConfig(args[1]);
		
		taskTracker = new TaskTracker(port, clusterConfig);
		
		try {
			//taskTracker.initialize();
			taskTracker.startAllWorkers();
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
