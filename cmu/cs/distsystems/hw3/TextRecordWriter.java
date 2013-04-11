package cmu.cs.distsystems.hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class TextRecordWriter {
	public static final String DEFAULT_DELIM = "\\t";
	public static final String INCOMPLETE_STRING = ".INCOMP";
	
	private String outputFile;
	private boolean isInit;
	private BufferedWriter bw;
	private String delim;
	private String tmpSuffix;

	public TextRecordWriter(String outputFile, String delimiter) {
		this.outputFile = outputFile;
		this.isInit = false;
		
		this.delim = DEFAULT_DELIM;
		if(delimiter != null) {
			this.delim = delimiter;
		}
		
		this.tmpSuffix = UUID.randomUUID().toString() + INCOMPLETE_STRING;
		
	}
	
	public void writeRecord(Record<String, String> record) throws IOException {
		if(!isInit) {
			initialize();
		}
		
		bw.append(record.getKey() + this.delim + record.getValue() + "\n");
	}
	
	
	
	private void initialize() throws IOException {
		bw = new BufferedWriter(new FileWriter(outputFile + tmpSuffix, true));
        isInit = true;
	}
	
	public void close() throws IOException {
		bw.flush();
		bw.close();
		
/*		FOR DEBUGGING
 		try {
			Thread.sleep(4000);
		} catch (Exception e) {
			
		}*/
		
		//Rename the file to the actual output name.
		File oldFile = new File(outputFile + tmpSuffix);
		if(oldFile.exists()) {
			File newFile = new File(outputFile);
			oldFile.renameTo(newFile);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
