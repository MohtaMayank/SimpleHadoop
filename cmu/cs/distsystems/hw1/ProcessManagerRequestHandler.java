package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProcessManagerRequestHandler implements Runnable {
	
	private static final String TRANSFER_PROCESS_TO = "TransferProcess";
	private static final String RUN_PROCESS = "RunProcess";
	private static final String CHECKPOINT_PROCESS = "CheckPointProcess";
	private	Socket clientSocket;
	private ProcessManager pm;
	
	public ProcessManagerRequestHandler(ProcessManager pm, Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.pm = pm;
	}
	
	public void handleSlavePMRequest(Message msg) {
		String type = msg.getType();
		
		if(type.equals(TRANSFER_PROCESS_TO)){
				TransferDetails td = (TransferDetails) msg.getObjToTransfer();
				//TODO: Transfer
        }else if(type.equals(RUN_PROCESS)){
				MigratableProcess p = (MigratableProcess) msg.getObjToTransfer();
				//TODO: resume process p.
        } else if(type.equals(CHECKPOINT_PROCESS)){
				MigratableProcess p2 = (MigratableProcess) msg.getObjToTransfer();
				//TODO: Checkpoint process p2 (save to disk)
		}
			
	}
	
	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			Message msg = (Message)ois.readObject();
			clientSocket.close();
			
			handleSlavePMRequest(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public void transferProcess(TransferDetails td) {
		int numRetries = 0;
		//Suspend the existing thread.
		ProcessHandle ph = null;
		try {
			ph = pm.getProcessMap().get(td.getProcessId());
			if(ph == null) {
				return;
			} 
			ph.getRef().suspend();
			ph.getFuture().get();
			
			synchronized (pm.getProcessMap()) {
				ph = pm.getProcessMap().get(td.getProcessId());
				if(ph == null) {
					return;
				} else {
					pm.getProcessMap().remove(ph.getRef().getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//TODO: Add retries??
		try  {
			
			//Transfer the process to the slave host.
			Socket sock = new Socket(td.getDestHost(), td.getDestPort());
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(new Message(RUN_PROCESS, ph.getRef()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
