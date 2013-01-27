package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
    private Class<? extends ProcessManagerRequestHandler> handlerType;
	
	public ProcessManagerServer(ProcessManager parentPM,
                                Class<? extends ProcessManagerRequestHandler> handlerType){
		this.parentPM = parentPM;
        this.handlerType = handlerType;
	}

    public ProcessManagerRequestHandler getNewHandler (ProcessManager pm, Socket client){

        Constructor ctor = null;
        try {
            ctor = handlerType.getConstructor(ProcessManager.class, Socket.class);
            Object[] varargs = {pm,client};
            ProcessManagerRequestHandler pmrh = (ProcessManagerRequestHandler) ctor.newInstance(varargs);

            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates
            } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;

    }
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			Executor e = Executors.newCachedThreadPool(); 
			serverSocket = new ServerSocket(4444);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				e.execute(this.getNewHandler(parentPM, clientSocket));
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
