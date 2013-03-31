package cmu.cs.distsystems.hw3;

import java.io.*;
import java.net.Socket;
import java.util.List;


/**
 * This is the client which the map-reduce application calls to submit a job.
 *  
 * @author mayank
 */

public class JobClient {


    final String configFile = "";
    static ClusterConfig clusterConfig;

	public void submitJob(Job job) {
		//Ask Job Tracker for a new job Id.
		//Check output specification for the job. Error if output dir already exists.
		
		//Compute input splits for the job.
		
		//Copy the resources needed to run the job. (Jar file, config file, computed splits)
		//Might not be needed if we use AFS.
		
		//Make an RPC call to JobTracker to start the job.

        String host = clusterConfig.getJobTrackerHost();
        int port = clusterConfig.getJobTrackerPort();

        Socket sock = null;

        try {
            sock = new Socket(host,port);
            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
            int newJobId = (Integer) ois.readObject();
            List<Split> splits = computeSplits();

            job.setId(newJobId);
            job.setSplits(splits);

            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(job);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            String message = bufferedReader.readLine();

            while(message != null){
                System.out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
	private static List<Split> computeSplits() {
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
