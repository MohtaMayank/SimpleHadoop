package cmu.cs.distsystems.hw3;

import java.io.Serializable;


/**
 * Represents a partition of file. A "split" which is typically
 * proccessed by one map task might contain multiples  of file
 * partitions 
 * @author mayank
 */
public class FilePartition implements Serializable {
	private String fileName; //File name
	private long start;	//start offset 
	private long end;	//end offset

	public FilePartition(String fileName, long start, long end){
		this.fileName = fileName;
		this.start = start;
		this.end = end;
	}
	

	public String getFileName() {
		return fileName;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
