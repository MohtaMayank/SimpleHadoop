package cmu.cs.distsystems.hw3.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private String simplemrJar;
	
	public ClusterConfig(String configFile) {
		this.configFile = configFile;
		
		parseConfigFile();
	}
	
	/**
	 * Parse the configuration file and populate all the 
	 * fields in configuration
	 */
	public void parseConfigFile() {
		BufferedReader br;
		int currentWorkerNum = 0;
		Map<String, String> map = new HashMap<String, String>();
		try {
			System.out.println(InetAddress.getLocalHost().getHostName());
			br = new BufferedReader(new FileReader(new File(this.configFile)));
			String line = null;
			while( (line = br.readLine()) != null ) {
				if(line.equals("") || line.startsWith("#")) {
					continue;
				}
				//Naive parsing - A very big if else.
				String[] toks = line.split(":");
				if(toks[0].equals("workerConfigNum")) {
					currentWorkerNum++;
				} else {
					if(toks[0].startsWith("worker")) {
						map.put(toks[0] + currentWorkerNum, toks[1]);
					} else {
						map.put(toks[0], toks[1]);
					}
				}
			}
			
			jobTrackerHost = map.get("jobTrackerHost");
            simplemrJar = map.get("mrJar");
            jobTrackerClientCommPort = Integer.parseInt(map.get("jobTrackerClientCommPort"));
			jobTrackerWorkerCommPort = Integer.parseInt(map.get("jobTrackerWorkerCommPort"));
			numWorkers = Integer.parseInt(map.get("numWorkers"));
			
			workers = new ArrayList<WorkerConfig>();
			
			for(int i = 1; i <= numWorkers; i++) {
				WorkerConfig w1 = new WorkerConfig(map.get("workerHost" + i), 
						Integer.parseInt(map.get("workerPort"+i)));
				w1.setNumMapSlots(Integer.parseInt(map.get("workerMapSlots"+i)));
				w1.setNumReduceSlots(Integer.parseInt(map.get("workerReduceSlots"+i)));
				workers.add(w1);
			}
			
		} catch (Exception e) {
			System.out.println("ERROR! Could not read the config file correctly");
			e.printStackTrace();
		}
		
	}
	
	//TODO: Convert this to suitable config file
	public void parseConfigFile2() {
		jobTrackerHost = "localhost";
		jobTrackerClientCommPort = 4000;
		jobTrackerWorkerCommPort = 6000;
		numWorkers = 2;
		workers = new ArrayList<WorkerConfig>();
		
		try {
			WorkerConfig w1 = new WorkerConfig(
					InetAddress.getLocalHost().getHostName(), 6001);
			w1.setNumMapSlots(2);
			w1.setNumReduceSlots(2);
			workers.add(w1);
			
			WorkerConfig w2 = new WorkerConfig(
					InetAddress.getLocalHost().getHostName(), 6011);
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

    public String getSimplemrJar() {
        return simplemrJar;
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