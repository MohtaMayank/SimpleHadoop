package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * CAUTION: Cannot be used with any sort of BufferedReader. Because it messes
 * up the position when it buffers more than what user has asked for.
 * 
 * Ideally we should implement TransactionalFileInputReader.
 * 
 * @author mayank
 */
public class TransactionalFileInputStream extends java.io.InputStream implements Serializable {

	private long position;
	private String fileName;
	private transient RandomAccessFile raf = null;
	
	public TransactionalFileInputStream(String fileName) {
		this.fileName = fileName;
		this.position = 0;
	}
	
	private void initialize() {
		try {
			this.raf = new RandomAccessFile(fileName, "r");
			this.raf.seek(position);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int read() throws IOException {
		//Seek to the position in file
		if(raf == null) {
			initialize();
		}
		
		int b = raf.read();
		this.position = raf.getFilePointer();
		//Start reading the file from the position
		return b; 
		
	}
	
	/*
	 * For testing
	 */
	public static void main(String[] args) {
		TransactionalFileInputStream tfis = new TransactionalFileInputStream("/home/mayank/testFile");
		
		DataInputStream dis = new DataInputStream(tfis);
		
		try {
			String line;
			while( (line = dis.readLine()) != null) {
				System.out.println(line);
				File objFile = new File("/home/mayank/testFile_TFIS.dat");
				ObjectOutputStream oos = new ObjectOutputStream(new 
						FileOutputStream(objFile));
				oos.writeObject(tfis);
				oos.flush();
				oos.close();
				dis.close();
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objFile));
				tfis = (TransactionalFileInputStream) ois.readObject();
				dis = new DataInputStream(tfis);
				ois.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
