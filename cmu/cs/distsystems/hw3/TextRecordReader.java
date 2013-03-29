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
	
	public Record<String, String> readNextRecord() throws IOException {
		if(!isInit) {
			initialize();
		}
		
		if(currentOffset >= filePartition.getEnd()) {
			raf.close();
			return null;
		}
		
		String line = raf.readLine();
		if(line == null) {
			return null;
		}
		
		Record<String, String> record;
		
		if(keyDelimiter == null) {
			record = new Record<String, String>(filePartition.getFileName() 
					+ currentOffset, line);
		} else {
			String[] tokens = line.split(keyDelimiter);
			if(tokens.length != 2) {
				System.out.println("error! wrong input format");
			}
			record = new Record<String, String>(tokens[0], tokens[1]);
		}
		
		currentOffset += line.length();
		
		return record;
	}
	
	private void initialize() throws IOException {
		//Random Access File
		raf = new RandomAccessFile(filePartition.getFileName(), "r");
		
		if(filePartition.getStart() != 0) {
			raf.seek(filePartition.getStart() - 1);
			//If this is not the start of line, then skip this line.
			String line = raf.readLine();
			if(line.equals("")) {
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
		
		this.isInit = true;
	}
	
	private void close() {
		
	}
	
	/**
	 * For testing!
	 */
	public static void main(String[] args) {
		try {
		
			String f1 = "/home/mayank/DistributedSystems/test1.txt";
			File file1 = new File(f1);
			FilePartition fp1 = new FilePartition(f1, 13, file1.length());
			
			TextRecordReader reader = new TextRecordReader(fp1, null);
			Record<String, String> record;
			while( (record = reader.readNextRecord()) != null ) {
				System.out.println(record.getKey() + ":" + record.getValue());
			}
			
			String f2 = "/home/mayank/DistributedSystems/test1.txt";
			File file2 = new File(f2);
			FilePartition fp2 = new FilePartition(f2, 0, 13);
			
			reader = new TextRecordReader(fp2, null);
			while( (record = reader.readNextRecord()) != null ) {
				System.out.println(record.getKey() + ":" + record.getValue());
			}
			
			String f3 = "/home/mayank/DistributedSystems/test1.txt";
			File file3 = new File(f3);
			FilePartition fp3 = new FilePartition(f3, 14, file3.length());
			
			reader = new TextRecordReader(fp3, null);
			while( (record = reader.readNextRecord()) != null ) {
				System.out.println(record.getKey() + ":" + record.getValue());
			}
		
			String f4 = "/home/mayank/DistributedSystems/test2.txt";
			File file4 = new File(f4);
			FilePartition fp4 = new FilePartition(f4, 0, file4.length());
			
			reader = new TextRecordReader(fp4, "\t");
			while( (record = reader.readNextRecord()) != null ) {
				System.out.println(record.getKey() + ":" + record.getValue());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
