package cmu.cs.distsystems.hw2;

import java.rmi.Remote;

public interface GreetingGiver extends Remote {

	public String giveGreeting(String name, Boolean throwEx) throws Exception;
	
	public String collectGreeting(String name, RemoteObjectRef remoteGreeter) throws Exception;
	
	public RemoteObjectRef locateGreeter(String greeterName) throws Exception;

}
