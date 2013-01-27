package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ProcessManagerRequestHandler implements Runnable {
	
	private static final String TRANSFER_PROCESS_TO = "TransferProcess";
	private static final String RESUME_PROCESS = "ResumeProcess";
	private static final String CHECKPOINT_PROCESS = "CheckPointProcess";
	
	private	Socket clientSocket;
	private ProcessManager pm;
	
	public ProcessManagerRequestHandler(ProcessManager pm, Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.pm = pm;
	}
	
	public boolean handleRequest(Message msg) {
		String type = msg.getType();
		
		if(type.equals(TRANSFER_PROCESS_TO)){
				TransferDetails td = (TransferDetails) msg.getObjToTransfer();
				transferProcess(td);
				return true;
        }else if(type.equals(RESUME_PROCESS)){
				MigratableProcess mp = (MigratableProcess) msg.getObjToTransfer();
				resumeProcess(mp);
				return true;
        } else if(type.equals(CHECKPOINT_PROCESS)){
				MigratableProcess p2 = (MigratableProcess) msg.getObjToTransfer();
				//TODO: Checkpoint process p2 (save to disk)
				return true;
		}
			
		return false;
	}
	
	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			Message msg = (Message)ois.readObject();
			clientSocket.close();
			
			handleRequest(msg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	
	/**
	 * Suspends the process to be transferred, creates a connection
	 * to the host to which the transfer has to be performed and 
	 * sends the Migratable process over.
	 * @param td
	 */
	public void transferProcess(TransferDetails td) {
		
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
			oos.writeObject(new Message(RESUME_PROCESS, ph.getRef()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Resume the process mp on this host.
	 * @param mp
	 */
	public void resumeProcess(MigratableProcess mp) {
		mp.resume();
		pm.execute(mp);
	}

}
