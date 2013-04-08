package cmu.cs.distsystems.hw3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a split of input data which should be submitted to a Mapper
 * It is comprised of a list of FilePartitions which represent the chunks of 
 * data that this split contains 
 * @author mayank
 *
 */
public class Split implements Serializable {

    final static long MAX_SPLIT_SIZE = 80000;
	FilePartition filePartition;
	
	public Split(FilePartition fp) {
		this.filePartition = fp;
	}
	
	public FilePartition getFilePartition() {
		return filePartition;
	}
	
	@Override
	public String toString() {
		return filePartition.getFileName() + "<" + filePartition.getStart()
				+ "," + filePartition.getEnd() + ">";
	}
}
