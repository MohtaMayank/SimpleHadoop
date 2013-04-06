package cmu.cs.distsystems.hw3;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * This class holds the configurations for the cluster 
 * @author mayank
 */
public class ClusterConfig {
	private String configFile;
	
	private String jobTrackerHost;
	private int jobTrackerWorkerCommPort;
	private int jobTrackerClientCommPort;
	private int numWorkers;
	private List<WorkerConfig> workers;
	
	public ClusterConfig(String configFile) {
		this.configFile = configFile;
		
		parseConfigFile();
	}
	
	/**
	 * Parse the configuration file and populate all the 
	 * fields in configuration
	 */
	//TODO: Convert this to suitable config file
	public void parseConfigFile() {
		jobTrackerHost = "localhost";
		jobTrackerClientCommPort = 4000;
		jobTrackerWorkerCommPort = 5000;
		numWorkers = 2;
		workers = new ArrayList<WorkerConfig>();
		
		try {
			WorkerConfig w1 = new WorkerConfig(
					InetAddress.getLocalHost().getHostName(), 5001);
			w1.setNumMapSlots(2);
			w1.setNumReduceSlots(2);
			workers.add(w1);
			
			WorkerConfig w2 = new WorkerConfig(
					InetAddress.getLocalHost().getHostName(), 5010);
			w2.setNumMapSlots(2);
			w2.setNumReduceSlots(2);
			workers.add(w2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Returns the worker  config object corresponding to the worker.
	 * @param id: "hostname:port"
	 * @return
	 */
	public WorkerConfig getWorkerConfig(String id) {
		for(WorkerConfig config : this.workers) {
			if(config.getUid().equals(id)) {
				return config;
			}
		}
		return null;
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public String getJobTrackerHost() {
		return jobTrackerHost;
	}

	public int getJobTrackerWorkerCommPort() {
		return jobTrackerWorkerCommPort;
	}

	public int getJobTrackerClientCommPort() {
		return jobTrackerClientCommPort;
	}

	public int getNumWorkers() {
		return numWorkers;
	}

	public List<WorkerConfig> getWorkers() {
		return workers;
	}
	
}

class WorkerConfig {
	private String hostName;
	private int port;
	
	private String uid; //"hostName:port"
	
	private int numMapSlots;
	private int numReduceSlots;

	public WorkerConfig(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
		this.uid = hostName + ":" + port;
	}
	
	public String getHostName() {
		return hostName;
	}
	public int getPort() {
		return port;
	}
	public String getUid() {
		return uid;
	}
	public int getNumMapSlots() {
		return numMapSlots;
	}
	public void setNumMapSlots(int numMapSlots) {
		this.numMapSlots = numMapSlots;
	}
	public int getNumReduceSlots() {
		return numReduceSlots;
	}
	public void setNumReduceSlots(int numReduceSlots) {
		this.numReduceSlots = numReduceSlots;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkerConfig other = (WorkerConfig) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}