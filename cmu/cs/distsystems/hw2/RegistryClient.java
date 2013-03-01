package cmu.cs.distsystems.hw2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import cmu.cs.distsystems.hw1.Message;

public class RegistryClient {
	int port;
	String registryHost;

	
	public RegistryClient(int port, String registryHost) {
		this.port = port;
		this.registryHost = registryHost;
	}
	
	/**
	 * Connects to registry server and looks up the remote object reference
	 * for an object with given id.
	 * @return The remote object reference if one exists else null
	 * @throws Exception: If there is some problem connecting to the registry server.
	 */
	public RemoteObjectRef lookup(String id) throws Exception {
		//Create suitable request message
		RegistryRequestMessage msg = new RegistryRequestMessage(
				RegistryServer.OP_LOOKUP, id, null);
		
		RemoteObjectRef ret = null;
		//Call the registry server
		ReturnMessage retMsg = callRegistryServer(msg);
		
		ret = (RemoteObjectRef)retMsg.getReturnVal();
		
		return ret;
	}
	
	/**
	 * Binds the remote object reference with the id and registers it
	 * with the RMI registry for clients to lookup
	 * @param ref : Remote object reference to register
	 * @param id : The id to use for registering.
	 * @throws Exception : Exception thrown if can't connect to the server or if 
	 * a remote object with this id already exists. In latter ccase use rebind if needed.
	 */
	public void bind(RemoteObjectRef ref) throws Exception {
		//Create suitable request message
		RegistryRequestMessage msg = new RegistryRequestMessage(
				RegistryServer.OP_BIND, ref.getObjectId(), ref);
		
		//Call the registry server
		ReturnMessage retMsg = callRegistryServer(msg);
		
		if(!retMsg.isException()) {
			System.out.println("Successfully bind object with id: " + ref.getObjectId());
		}
		
	}
	
	/**
	 * Like bind, but would overwrite the entry in rmi registry if 
	 * one already exists.
	 * @param ref : Remote object reference to register
	 * @param id : The id to use for registering.
	 */
	public void rebind(RemoteObjectRef ref) throws Exception {
		//Create suitable request message
		RegistryRequestMessage msg = new RegistryRequestMessage(
				RegistryServer.OP_REBIND, ref.getObjectId(), ref);
		
		//Call the registry server
		ReturnMessage retMsg = callRegistryServer(msg);
		
		if(!retMsg.isException()) {
			System.out.println("Successfully unbind object with id: " + ref.getObjectId());
		}
	}
	
	/**
	 * To fetch a list of all remote objects registered with this
	 * registry.
	 * @return
	 */
	public List<RemoteObjectRef> getAllRemoteObjects() {
		
		return null;
	}
	

	private ReturnMessage callRegistryServer(RegistryRequestMessage msg) throws Exception {
		
		Socket sock = null;
		ReturnMessage retMsg = null;
		
		try {
			sock = new Socket(registryHost, port);
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			retMsg = (ReturnMessage) ois.readObject();
			
			if(retMsg.isException()) {
				throw (RMIRemoteException)retMsg.getReturnVal();
			}
			
			oos.close();
			ois.close();
			sock.close();
		} catch (RMIRemoteException e) {
			throw e;
		} catch (Exception e1) {
			//TODO: add logic for retries?
			throw e1;
		} finally {
			try {
				if(sock != null && !sock.isClosed()) {
					sock.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return retMsg;
	}
	
	/**
	 * For testing
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RegistryClient cl = LocateRegistry.getRegistryClient("localhost", 
				RegistryServer.DEFAULT_REGISTRY_PORT);
			RemoteObjectRef ror = cl.lookup("HelloGiver1");
			System.out.println(ror.getRemoteInterfaceName() + " " + ror.getObjectId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
