package cmu.cs.distsystems.hw2;

import java.rmi.Remote;
import java.util.List;

public class RegistryClient {
	int port;
	String registryHost;

	/**
	 * Connects to registry server and looks up the remote object reference
	 * for an object with given id.
	 * @return The remote object reference if one exists else null
	 * @throws Exception: If there is some problem connecting to the registry server.
	 */
	public RemoteObjectRef lookup(String id) throws Exception {
		
		//Connect to registry server.
		
		//lookup the remote object reference.
		
		return null;
	}
	
	/**
	 * Binds the remote object reference with the id and registers it
	 * with the RMI registry for clients to lookup
	 * @param ref : Remote object reference to register
	 * @param id : The id to use for registering.
	 * @throws Exception : Exception thrown if can't connect to the server or if 
	 * a remote object with this id already exists. In latter ccase use rebind if needed.
	 */
	public void bind(RemoteObjectRef ref, String id) throws Exception {

	}
	
	/**
	 * Like bind, but would overwrite the entry in rmi registry if 
	 * one already exists.
	 * @param ref : Remote object reference to register
	 * @param name : The id to use for registering.
	 */
	public void rebind(RemoteObjectRef ref, String name) {
	
	}
	
	/**
	 * De-register a remote object from the rmi registry 
	 * @param name
	 */
	public void unbind(String name) {

	}
	
	/**
	 * To fetch a list of all remote objects registered with this
	 * registry.
	 * @return
	 */
	public List<RemoteObjectRef> getAllRemoteObjects() {
		
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
