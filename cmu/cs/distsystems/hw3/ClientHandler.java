package cmu.cs.distsystems.hw3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    Socket client;
    JobTracker jobTracker;


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

        JobStatus js = new JobStatus(job);
        List<Task> mapTasks = getMapTasks(job);
        List<Task> reduceTasks = getReduceTasks(job);

        setJobStatus(js,mapTasks,reduceTasks);

        for(Task task:mapTasks){
            jobTracker.pendingTasks.add(task);
        }
        for(Task task:reduceTasks){
            jobTracker.pendingTasks.add(task);
        }

        jobTracker.status.put(job.getId(),js);

    }

    private String getMessage(JobStatus js){
        return "update~";
    }

    public ClientHandler(Socket client,JobTracker jobTracker){
        this.client = client;
        this.jobTracker = jobTracker;
    }

    @Override
    public void run() {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(JobTracker.getNewJobId());

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            Job job = (Job)ois.readObject();

            setUpJob(job);

            PrintWriter pw = new PrintWriter(client.getOutputStream());

            JobStatus js = jobTracker.status.get(job.getId());

            while(!js.jobFinished){
                try {
                    Thread.sleep(3000);
                    pw.write(getMessage(js));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
