package cmu.cs.distsystems.hw1;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class HeartBeatThread implements Runnable {

	private String masterHost;
	private int masterPort;
	
	public HeartBeatThread(ProcessManager parentPM) {
		this.masterHost = masterHost;
		this.masterPort = masterPort;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Socket socket = new Socket(masterHost, masterPort);
				ObjectOutputStream oos = new ObjectOutputStream( 
						socket.getOutputStream());
				//TODO: create a proper heart beat message.
				oos.writeObject(new HeartBeat());
				oos.flush();
				oos.close();
				socket.close();
				
				Thread.sleep(5000);
				
			}
		} catch (Exception e) {
			
		}
		
		
	}

}
