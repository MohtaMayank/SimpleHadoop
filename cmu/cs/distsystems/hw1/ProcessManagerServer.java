package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listens on a port and accepts requests from other process managers
 * (either master or slave).
 * On receiving a request, it spawns a new RequestHandler thread
 * which reads data from socket and performs required actions
 * @author mayank
 *
 */
public class ProcessManagerServer implements Runnable {

	private ProcessManager parentPM;
	
	public ProcessManagerServer(ProcessManager parentPM){
		this.parentPM = parentPM;
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			Executor e = Executors.newCachedThreadPool(); 
			serverSocket = new ServerSocket(4444);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				e.execute(new ProcessManagerRequestHandler(parentPM, clientSocket));
			}
		} catch (Exception e) {
			//Close the socket
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
