package cmu.cs.distsystems.hw3;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the client which the map-reduce application calls to submit a job.
 *  
 * @author mayank
 */

public class JobClient {

	public static void submitJobAndWaitForCompletion(Job job) {
		ClusterConfig clusterConfig = new ClusterConfig(job.getConfigFile());
		//Ask Job Tracker for a new job Id.
		//Check output specification for the job. Error if output dir already exists.
		
		//Compute input splits for the job.
		
		//Copy the resources needed to run the job. (Jar file, config file, computed splits)
		//Might not be needed if we use AFS.
		
		//Make an RPC call to JobTracker to start the job.

        String host = clusterConfig.getJobTrackerHost();
        int port = clusterConfig.getJobTrackerClientCommPort();

        Socket sock = null;

        try {
        	List<Split> splits = computeSplits(job);
        	job.setSplits(splits);
        	
        	JobProgress progress;
        	//Indicates that requesting new Id
        	job.setId(-1);
        	
            sock = new Socket(host,port);

            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(job);
            oos.flush();
            
            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
            progress = (JobProgress) ois.readObject();
            
            job.setId(progress.getJobId());
            
            oos.close();
            ois.close();
            sock.close();
            
            //Poll for job status periodically.
            boolean inProgress = true;
            do {
            	Thread.sleep(1000);
            	sock = new Socket(host,port);
            	oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(job);
                oos.flush();
                
                ois = new ObjectInputStream(sock.getInputStream());
                progress = (JobProgress) ois.readObject();
                
                oos.close();
                ois.close();
                sock.close();
            	inProgress = (progress.getState() != JobStatus.JobState.SUCCESS) &&
            			(progress.getState() != JobStatus.JobState.FAILED);
            } while (inProgress);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

	//TODO: Implement this!
	private static List<Split> computeSplits(Job job) {
		
		List<Split> splits = new ArrayList<Split>();
		
		File inputDir = new File(job.getInputDir());
		for (final File fileEntry : inputDir.listFiles()) {
			long bytes = fileEntry.length();
			if(bytes > Split.MAX_SPLIT_SIZE) {
				long numChunks = (bytes/Split.MAX_SPLIT_SIZE) + 1;
				long chunkSize = ((bytes/numChunks));
				long start, end;
				for(int i = 0; i < numChunks; i++) {
					start = i*chunkSize;
					end = (i+1)*chunkSize;
					if(i == numChunks - 1) {
						end = bytes;
					}
					
					splits.add(new Split(new FilePartition(
							fileEntry.getAbsolutePath(), start, end)));
				}
				
			}
		}
		
		return splits;
	}

}
