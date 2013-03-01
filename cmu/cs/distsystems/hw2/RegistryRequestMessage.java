package cmu.cs.distsystems.hw2;

import java.io.Serializable;


/**
 * This class represents a message to RMI registry requesting 
 * one of the operations bind, unbind, rebind, lookup, getRemoteObjectList etc
 * @author mayank
 *
 */
public class RegistryRequestMessage implements Serializable {
	private static final long serialVersionUID = -2530623396183173852L;
	
	String registryOperation;
	String id;
	RemoteObjectRef ror;

	private RegistryRequestMessage() {
		
	}
	
	public RegistryRequestMessage(String operation, String id, RemoteObjectRef ror) {
		this.registryOperation = operation;
		this.id = id;
		this.ror = ror;
	}
	
	public String getRegistryOperation() {
		return registryOperation;
	}

	public String getId() {
		return id;
	}

	public RemoteObjectRef getRor() {
		return ror;
	}


	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
