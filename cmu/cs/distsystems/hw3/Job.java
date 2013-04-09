package cmu.cs.distsystems.hw3;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Map Reduce Job.
 * It contains all information like number of tasks, whether they are completed or running, where are
 * they running, etc 
 * @author mayank
 */

public class Job implements Serializable {

	private int id;
	
	private int numReducers;
	private String jar;
	private String mapClass;
	private String combinerClass;
	private String reduceClass;
	private String configFile;
	private String inputDir;
	private String outputDir;

	private List<Split> splits;
	
	public Job() {
		this.id = -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumReducers() {
		return numReducers;
	}

	public void setNumReducers(int numReducers) {
		this.numReducers = numReducers;
	}

	public String getJar() {
		return jar;
	}

	public void setJar(String jar) {
		this.jar = jar;
	}

	public String getMapClass() {
		return mapClass;
	}

	public void setMapClass(String mapClass) {
		this.mapClass = mapClass;
	}

	public String getCombinerClass() {
		return combinerClass;
	}

	public void setCombinerClass(String combinerClass) {
		this.combinerClass = combinerClass;
	}

	public String getReduceClass() {
		return reduceClass;
	}

	public void setReduceClass(String reduceClass) {
		this.reduceClass = reduceClass;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public String getTmpMapOpDir() {
		return outputDir + "/tmp" + getId() + "/";
	}

	public List<Split> getSplits() {
		return splits;
	}

	public void setSplits(List<Split> splits) {
		this.splits = splits;
	}

    public int getNumSplits(){
        return this.splits.size();
    }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
