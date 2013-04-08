package cmu.cs.distsystems.hw3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cmu.cs.distsystems.hw3.JobStatus.JobState;

public class ClientHandler implements Runnable {

    JobTracker jobTracker;

    public ClientHandler(JobTracker jobTracker){
        this.jobTracker = jobTracker;
    }

    private List<Task> getMapTasks(Job job){

        List<Task> tasks = new ArrayList<Task>();

        for(Split sp:job.getSplits()){
            tasks.add(new MapTask(job,JobTracker.getNextTaskId(),sp));
        }

        return tasks;

    }

    private List<Task> getReduceTasks(Job job){
        List<Task> tasks = new ArrayList<Task>();

        for(int i = 0 ; i < job.getNumReducers(); i++){
            tasks.add(new ReduceTask(job,JobTracker.getNextTaskId(), i));
        }

        return tasks;
    }

    private void setJobStatus(JobStatus js,List<Task> mapTasks,List<Task> reduceTasks){

        for(Task t:mapTasks){
            js.mapTasks.put(t.getTaskId(),t);
        }

        for(Task t:reduceTasks){
            js.reduceTasks.put(t.getTaskId(),t);
        }
        
    }

    private void setUpJob(Job job){
    	//Create the job status object.
        JobStatus js = new JobStatus(job);
        List<Task> mapTasks = getMapTasks(job);
        List<Task> reduceTasks = getReduceTasks(job);

        setJobStatus(js,mapTasks,reduceTasks);
        js.setJobState(JobState.PENDING);

        //Insert into the map
        jobTracker.getStatus().put(job.getId(), js);
        
        //Insert all tasks into pending queue
        for(Task task:mapTasks){
            jobTracker.getPendingMapTasks().add(task);
        }

        /*for(Task task:reduceTasks){
            jobTracker.getPendingReduceTasks().add(task);
        }*/

    }

    @Override
    public void run() {
    	ServerSocket server = null;
    	try {
    		server = new ServerSocket(jobTracker.getClientCommPort());
    		
    		Socket clientSocket;
    		
    		while(true) {
	    		clientSocket = server.accept();
	    		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				
				Job job = (Job)ois.readObject();
				
				if(job.getId() == -1) {
					job.setId(JobTracker.getNextJobId());
					setUpJob(job);
				}
				
				JobStatus status = jobTracker.getStatus().get(job.getId());
				
				JobProgress progress = new JobProgress(job.getId());

				//TODO: set this properly
				progress.setPercentMapTaskFinished(0);
				progress.setPercentReduceTaskFinished(0);
				progress.setState(status.getJobState());
				
				ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(progress);
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

}
