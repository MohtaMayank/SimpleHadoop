package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;

/**
 * Before suspending, the BufferedWriter / PrintWriter or any other wrapper 
 * should be flushed. Else  the writes may be lost. 
 * @author mayank
 */
public class TransactionalFileOutputStream extends java.io.OutputStream implements Serializable {

	boolean append = false;
	String pathname;
	private transient RandomAccessFile raf = null; 
	
	public TransactionalFileOutputStream(String pathname, boolean append) {
		this.append = append;
		this.pathname = pathname;
		initialize();
	}
	
	public void initialize() {
		File f = new File(pathname);
		if(!append) {
			if( f.exists() ) {
				f.delete();
			}
		}
		
		try {
			//TODO: Should we "rwd" mode?
			raf = new RandomAccessFile(f, "rwd");
			raf.seek(f.length());
			//System.out.println(f.length());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.append = true;
	}

	@Override
	public void write(int b) throws IOException {
		if(raf == null) {
			initialize();
		}
		raf.write(b);
	}
	
	/*
	 * For testing
	 */
	public static void main(String[] args) {
		TransactionalFileInputStream tfis = new TransactionalFileInputStream("/home/mayank/testFile");
		TransactionalFileOutputStream tfos = new TransactionalFileOutputStream(
				"/home/mayank/testFile_copy", false);
		
		DataInputStream dis = new DataInputStream(tfis);
		PrintWriter pw = new PrintWriter(tfos);
		
		try {
			String line;
			while( (line = dis.readLine()) != null) {
				pw.println(line);
				pw.flush();
				
				File tfisFile = new File("/home/mayank/testFile_TFIS.dat");
				ObjectOutputStream oos = new ObjectOutputStream(new 
						FileOutputStream(tfisFile));
				oos.writeObject(tfis);
				oos.flush();
				oos.close();
				File tfosFile = new File("/home/mayank/testFile_TFOS.dat");
				oos = new ObjectOutputStream(new FileOutputStream(tfosFile));
				oos.writeObject(tfos);
				oos.flush();
				oos.close();
				
				dis.close();
				
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tfisFile));
				tfis = (TransactionalFileInputStream) ois.readObject();
				dis = new DataInputStream(tfis);
				ois.close();
				
				ois = new ObjectInputStream(new FileInputStream(tfosFile));
				tfos = (TransactionalFileOutputStream) ois.readObject();
				pw = new PrintWriter(tfos);
				ois.close();
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
