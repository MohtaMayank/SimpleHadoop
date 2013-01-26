package cmu.cs.distsystems.hw1;

import java.io.ObjectInputStream;
import java.net.Socket;

public class ProcessManagerRequestHandler implements Runnable {
	
	private static final String TRANSFER_PROCESS_TO = "TransferProcess";
	private static final String RUN_PROCESS = "RunProcess";
	private static final String CHECKPOINT_PROCESS = "CheckPointProcess";
	private	Socket clientSocket;
	
	public ProcessManagerRequestHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			String type = (String)ois.readObject();
			
			switch(type) {
				case TRANSFER_PROCESS_TO:
					TransferDetails td = (TransferDetails) ois.readObject();
					//TODO: Transfer
					break;
				case RUN_PROCESS:
					MigratableProcess p = (MigratableProcess) ois.readObject();
					//TODO: resume process p.
					break;
				case CHECKPOINT_PROCESS:
					MigratableProcess p2 = (MigratableProcess) ois.readObject();
					//TODO: Checkpoint process p2 (save to disk)
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
