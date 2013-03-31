package cmu.cs.distsystems.hw3;

import java.util.List;

/**
 * This class holds the configurations for the cluster 
 * @author mayank
 */
public class ClusterConfig {
	private String configFile;
	
	private String jobTrackerHost;
	private int jobTrackerPort;
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
	public void parseConfigFile() {
		
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public String getJobTrackerHost() {
		return jobTrackerHost;
	}

	public int getJobTrackerPort() {
		return jobTrackerPort;
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