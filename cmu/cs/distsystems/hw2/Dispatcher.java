package cmu.cs.distsystems.hw2;

import java.rmi.Remote;
import java.util.HashMap;

/**
 * 
 * @author mayank
 */

public class Dispatcher {
	private int port = 5555; //Default port
	//Map to hold the mapping from
	private HashMap<String, Remote> localRefTable;
	
	//serves requests from default port
	public Dispatcher() {
		this.localRefTable = new HashMap<String, Remote>();
	}

	/**
	 * Serves requests from "port" port.
	 * @param port
	 */
	public Dispatcher(int port) {
		this.port = port;
		this.localRefTable = new HashMap<String, Remote>();
	}
	
	/**
	 * 
	 * @param id: The id of the remote object
	 * @param remoteInterface: The name of the remoteInterface which this object is
	 * exposed as. Can be any one of the many remote interfaces this object implements. 
	 * @param obj: The remote object to which this dispatcher can server requests.
	 * @return: After registering in the local table, an ror is created and returned. 
	 * This can be used for registering with the rmi registry.
	 */
	public RemoteObjectRef exportRemoteObject(String id, String remoteInterface, Remote obj) {
		
		return null;
	}
	
	
	/**
	 * This method starts serving RMI requests for remote objects on this server. 
	 * Once the server code is done registering all the remote objects it has 
	 * to offer with the dispatcher and registry,
	 * this method is started. It listens on the port for any incoming RMI requests,
	 * unmarshalls the message, calls the requested method on the requested remote 
	 * object and creates replies back to the requester with the results. 
	 */
	public void serve() {
		
	}

}
