package cmu.cs.distsystems.hw2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This is the registry server which should accept calls remote calls 
 * for bind(), rebind(), lookup() etc. RegistryClient is the wrapper
 * which will make these remote calls on behalf of the application
 * @author mayank
 */

public class RegistryServer {
	public static final String OP_BIND = "bind";
	public static final String OP_REBIND = "rebind";
	public static final String OP_UNBIND = "unbind";
	public static final String OP_LOOKUP = "lookup";
	
	public static final int DEFAULT_REGISTRY_PORT = 4444;
	
	//Table to store the id to RemoteObjectReference Mapping
	HashMap<String, RemoteObjectRef> remoteRefTable;
	
	int port = DEFAULT_REGISTRY_PORT; //Default rmi registry port
	
	
	public RegistryServer() {
		this.remoteRefTable = new HashMap<String, RemoteObjectRef>();
	}
	
	public RegistryServer(int port) {
		this.port = port;
		this.remoteRefTable = new HashMap<String, RemoteObjectRef>();
	}
	
	/**
	 * Start the registry server at the given port and accept 
	 * different requests from clients or servers 
	 */
	public void start() {
		ServerSocket serverSocket = null;
		try {
			Executor e = Executors.newCachedThreadPool(); 
			serverSocket = new ServerSocket(port);
			while(true) {
				Socket clientSocket = null;
				try {
					clientSocket = serverSocket.accept();
					
					//Should this part be in a separate thread using thread pool or
					//can add retries in RegistryClient as that is the only place through
					//which clients will interact with the RegistryServer.
					ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
					
					RegistryRequestMessage msg = (RegistryRequestMessage)ois.readObject();
					
					ReturnMessage retMsg = performOperation(msg);
					
					ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
					oos.writeObject(retMsg);
					oos.flush();
					
					ois.close();
					oos.close();
					clientSocket.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				} finally {
					clientSocket.close();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse the request message and return a suitable value. This method
	 * will be called only by the RegistryClient class. The RegistryClient
	 * class should be responsible for passing a suitable RegistryRequestMessage
	 * over the socket and giving a correct response to the application
	 * on different calls. 
	 * @param msg
	 * @return
	 */
	public ReturnMessage performOperation(RegistryRequestMessage msg) {
		ReturnMessage retMsg;
		String regOp = msg.getRegistryOperation();
		
		if(regOp.equals(OP_LOOKUP)) {
			retMsg = new ReturnMessage(remoteRefTable.get(msg.getId()), false);
		} else if (regOp.equals(OP_BIND)) {
			RemoteObjectRef ror = msg.getRor();
			if(ror != null) {
				if(remoteRefTable.get(msg.getId()) == null) {
					//Bind the object.
					remoteRefTable.put(msg.getId(), ror);
					retMsg = new ReturnMessage("OK", false);
				} else {
					retMsg = new ReturnMessage(new RMIRemoteException(
							"Object with id " + msg.getId() + " already bound"), true);
				}
			} else {
				retMsg = new ReturnMessage(new RemoteException(
						"Cannot bind null object"), true);
			}
		} else if(regOp.equals(OP_REBIND)) {
			RemoteObjectRef ror = msg.getRor();
			if(ror != null) {
				//Re-Bind the object.
				remoteRefTable.put(msg.getId(), msg.getRor());
				retMsg = new ReturnMessage("OK", false);
			} else {
				retMsg = new ReturnMessage(new RemoteException(
						"Cannot bind null object"), true);
			}
		} else if(regOp.equals(OP_UNBIND)) {
			remoteRefTable.remove(msg.getId());
			retMsg = new ReturnMessage("OK", false);
		} else {
			//Reaching here means Registry client is broken.
			retMsg = null;
		}
		
		return retMsg;
	}
	
	
	public static void main(String[] args) {
		//Fetch port from args and start a RegistryServer
		RegistryServer server = new RegistryServer();
		server.start();
	}

}
