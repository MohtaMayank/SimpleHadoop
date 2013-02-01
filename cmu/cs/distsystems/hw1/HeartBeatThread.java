package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HeartBeatThread implements Runnable {

    private ProcessManager parentPM;
	
	public HeartBeatThread(ProcessManager parentPM) {
        this.parentPM = parentPM;
	}




	@Override
	public void run() {
		Socket socket = null;
		try {
			while(true) {
				socket = new Socket(this.parentPM.getMasterHost(),
                        this.parentPM.getMasterPort());
				ObjectOutputStream oos = new ObjectOutputStream( 
						socket.getOutputStream());
				//TODO: create a proper heart beat message.
				oos.writeObject(new Message(Message.RECIEVE_HEART_BEAT,parentPM.getHostInformation()));
				oos.flush();
				oos.close();
				socket.close();
				
				Thread.sleep(5000);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
