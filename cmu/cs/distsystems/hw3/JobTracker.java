package cmu.cs.distsystems.hw3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import cmu.cs.distsystems.hw3.JobStatus.JobState;
import cmu.cs.distsystems.hw3.TaskTrackerHBResponse.Cmd;

/**
 * JobTracker starts and maintains the MapReduce Job.
 * JobTracker runs on master node and is responsible for scheduling tasks to different workers
 * It also has other responsibilities like monitoring, failure recovery etc. 
 * @author mayank
 */

public class JobTracker {

	static int nextJobId = 0;
	static int nextTaskId = 0;

	private ClusterConfig clusterConfig;
	
	private String host;
    private int workerCommPort;
    private int clientCommPort;
    
    private Queue<Task> pendingMapTasks;
    private Queue<Task> pendingReduceTasks;
    private Map<Integer,JobStatus> status;

    public JobTracker(ClusterConfig clusterConfig) {
    	this.clusterConfig = clusterConfig;
    	this.workerCommPort = clusterConfig.getJobTrackerWorkerCommPort();
    	this.clientCommPort = clusterConfig.getJobTrackerClientCommPort();
    	
        pendingMapTasks = new ConcurrentLinkedQueue<Task>();
        pendingReduceTasks = new ConcurrentLinkedQueue<Task>();
        status = new ConcurrentHashMap<Integer, JobStatus>();
    }
    
    public String getHost() {
		return host;
	}

	public int getWorkerCommPort() {
		return workerCommPort;
	}

	public int getClientCommPort() {
		return clientCommPort;
	}

	public static int getNextJobId() {
		return nextJobId;
	}

	public Queue<Task> getPendingMapTasks() {
		return pendingMapTasks;
	}

	public Queue<Task> getPendingReduceTasks() {
		return pendingReduceTasks;
	}


	public Map<Integer, JobStatus> getStatus() {
		return status;
	}    
    
    public static synchronized int getNewJobId(){
        nextJobId++;
        return nextJobId;
    }

    public static synchronized int getNextTaskId(){
        nextTaskId++;
        return nextTaskId;
    }

    private Task nextPendingMapTask(){

        Task mapTask = pendingMapTasks.poll();
        JobStatus js = status.get(mapTask.getParentJob().getId());

        if(js.jobState == JobState.PENDING){
            js.setJobState(JobState.MAP_RUNNING);
        }

        return mapTask;
    }

    private Task nextPendingReduceTask(){

        Task reduceTask = pendingReduceTasks.poll();
        JobStatus js = status.get(reduceTask.getParentJob().getId());

        if(js.jobState != JobState.REDUCE_RUNNING){
            js.setJobState(JobState.REDUCE_RUNNING);
        }

        return reduceTask;

    }

	public void run() {
		//Launch thread that will communicate with the clients.
		Thread clientThread = new Thread(new ClientHandler(this));
		clientThread.start();
		
		//set up loop to communicate with workers.
    	ServerSocket server = null;
    	try {
    		server = new ServerSocket(workerCommPort);
    		
    		Socket clientSocket;
    		
    		while(true) {
	    		clientSocket = server.accept();
	    		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				
	    		TaskTrackerHB taskTrackerHB = (TaskTrackerHB)ois.readObject();

	    		TaskTrackerHBResponse resp = handleHeartbeat(taskTrackerHB);
	    		
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(resp);
				oos.flush();
				
				ois.close();
				oos.close();
				clientSocket.close();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if(server != null) {
    				server.close();
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

	}
	
	private TaskTrackerHBResponse handleHeartbeat(TaskTrackerHB hb) {

        TaskTrackerHBResponse resp = null;
		
		//Update all statuses from the heartbeat
		for(Task t : hb.getTasksSnapshot()) {
			int jobId = t.getParentJob().getId();
			JobStatus jobStatus = status.get(jobId);
			if(t.getTaskType() == TaskType.MAP) {
				if(t.getState() == TaskState.FAILED) {
					//If any task fails more than a certain number then mark job as failed
					if(t.getAttemptNum() > 3) {
						jobStatus.setJobState(JobState.FAILED);
					} else {
						t.setTaskState(TaskState.PENDING);
						t.setPercentComplete(0);
						t.setAttemptNum(t.getAttemptNum() + 1);
						jobStatus.getMapTasks().put(t.getTaskId(), t);
						pendingMapTasks.add(t);
					}
					
				} else {
					jobStatus.getMapTasks().put(t.getTaskId(), t);
				}
			} else if (t.getTaskType() == TaskType.REDUCE ) {
                if(t.getState() == TaskState.FAILED) {
                    //If any task fails more than a certain number then mark job as failed
                    if(t.getAttemptNum() > 3) {
                        jobStatus.setJobState(JobState.FAILED);
                    } else {
                        t.setTaskState(TaskState.PENDING);
                        t.setPercentComplete(0);
                        t.setAttemptNum(t.getAttemptNum() + 1);
                        jobStatus.getMapTasks().put(t.getTaskId(), t);
                        pendingReduceTasks.add(t);
                    }

                } else {
                    jobStatus.getReduceTasks().put(t.getTaskId(), t);
                }
			}

		}

        for(int jobId:status.keySet()){
            JobStatus js = status.get(jobId);
            js.updateStatus();

            if(js.jobState == JobState.MAP_FINISHED){
                for(int taskId:js.reduceTasks.keySet()){
                    Task task = js.reduceTasks.get(taskId);
                    this.pendingReduceTasks.add(task);
                }
                js.setJobState(JobState.REDUCE_RUNNING);
                System.out.println("Reduce phase");
            }
        }


		//Assign new task to the worker
        List<Task> newTasks = new ArrayList<Task>();
        int freeSlots = hb.getNumFreeMapSlots();
		while (freeSlots > 0 && pendingMapTasks.size() > 0 ) {
			Task newTask = nextPendingMapTask();
			if( status.get(newTask.getParentJob().getId()).getJobState() != JobState.FAILED ) {
				newTasks.add(newTask);
				freeSlots--;
			}
		}
		freeSlots = hb.getNumFreeMapSlots();
		while (hb.getNumFreeReduceSlots() > 0 && pendingReduceTasks.size() > 0 ) {
			Task newTask = nextPendingReduceTask();
			if( status.get(newTask.getParentJob().getId()).getJobState() != JobState.FAILED ) {
				newTasks.add(newTask);
				freeSlots--;
			}
		}
		
		if(newTasks.size() == 0) { 
			resp = new TaskTrackerHBResponse(null, 
					TaskTrackerHBResponse.Cmd.POLL);
		} else {
			resp = new TaskTrackerHBResponse(newTasks, TaskTrackerHBResponse.Cmd.NEW_TASKS);
		}
		
		return resp;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JobTracker jt = new JobTracker( new ClusterConfig(args[0]) );
		jt.run();
	}

}
