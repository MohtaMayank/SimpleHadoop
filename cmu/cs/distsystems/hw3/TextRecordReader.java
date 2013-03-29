package cmu.cs.distsystems.hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *  
 * @author mayank
 */

public class TextRecordReader {

	private FilePartition filePartition;
	private String keyDelimiter;
	
	private long currentOffset;
	
	boolean isInit;
	RandomAccessFile raf;
	
	/**
	 * if keyDelimiter is null then "<fileLocation>:<byteOffset>" will be the default key
	 */
	public TextRecordReader(FilePartition filePartition, String keyDelimiter) {
		this.filePartition = filePartition;
		this.keyDelimiter = keyDelimiter;
		
		this.isInit = false;
	}
	
	public Record<String, String> readNextRecord() throws IOException{
		if(!isInit) {
			initialize();
		}
		if(currentOffset > filePartition.getEnd()) {
			close();
			return null;
		}
		
		
		//Logic to split a line into key and values and create a value.
		//String line = br.readLine();
		Record<String, String> record = null;
		
		return record;
	}
	
	private void initialize() throws IOException {
		//Random Access File
		raf = new RandomAccessFile(filePartition.getFileName(), "r");
		
		if(filePartition.getStart() != 0) {
			raf.seek(filePartition.getStart() - 1);
			//If this is not the start of line, then skip this line.
			String line = raf.readLine();
			if(line.equals("\n")) {
				raf.seek(filePartition.getStart());
				this.currentOffset = filePartition.getStart();
			} else {
				raf.seek(filePartition.getStart() + line.length());
				this.currentOffset = filePartition.getStart() + line.length();
			}
		} else {
			raf.seek(0);
			this.currentOffset = 0;
		}
		
	}
	
	private void close() {
		
	}
	
	/**
	 * For testing!
	 */
	public static void main(String[] args) {
		try {
		
		String f1 = "";
		File file1 = new File(f1);
		FilePartition fp1 = new FilePartition("", 0, f1.length());
		
		TextRecordReader reader = new TextRecordReader(fp1, null);
		Record<String, String> record;
		while( (record = reader.readNextRecord()) != null ) {
			
		}
		
		String f2 = "";
		File file2 = new File(f2);
		FilePartition fp2 = new FilePartition("", 5, f2.length());
		
		String f3 = "";
		File file3 = new File(f3);
		FilePartition fp3 = new FilePartition("", 5, 15);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
