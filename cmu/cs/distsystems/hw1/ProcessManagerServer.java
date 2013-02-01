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
    private int port;
	
	public ProcessManagerServer(ProcessManager parentPM, int serverPort,
                                Class<? extends ProcessManagerRequestHandler> handlerType){
		this.parentPM = parentPM;
        this.handlerType = handlerType;
        this.port = serverPort;
	}

    public ProcessManagerRequestHandler getNewHandler (ProcessManager pm, Socket client){

        Constructor ctor = null;
        try {
            ctor = handlerType.getConstructor(ProcessManager.class, Socket.class);
            Object[] varargs = {pm,client};
            ProcessManagerRequestHandler pmrh = (ProcessManagerRequestHandler) ctor.newInstance(varargs);
            return pmrh;
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
			serverSocket = new ServerSocket(port);
			while(true) {
				try {
					Socket clientSocket = serverSocket.accept();
					//System.out.println("Creating new thread");
					e.execute(this.getNewHandler(parentPM, clientSocket));
				} catch (Exception exc) {
					exc.printStackTrace();
				} 
			}
		} catch(Exception e) {
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
