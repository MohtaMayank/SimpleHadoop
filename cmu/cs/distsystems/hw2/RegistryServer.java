package cmu.cs.distsystems.hw2;

import java.util.HashMap;

/**
 * This is the registry server which should accept calls remote calls 
 * for bind(), rebind(), lookup() etc. RegistryClient is the wrapper
 * which will make these remote calls on behalf of the application
 * @author mayank
 */

public class RegistryServer {
	//Table to store the id to RemoteObjectReference Mapping
	HashMap<String, RemoteObjectRef> remoteRefTable;
	int port = 4444; //Default rmi registry port
	
	
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
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Fetch port from args and start a RegistryServer
		
	}

}
