package cmu.cs.distsystems.hw2;

import java.net.UnknownHostException;
import java.rmi.Remote;
import java.util.UUID;

/**
 * An example of how a server registers remote objects, registers them 
 * and starts serving requests 
 * @author mayank
 */

class Simple implements Remote {
    public String hello(String msg){
        System.out.println("hello " + msg);
        return "yes";
    }
}

public class SimpleServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws UnknownHostException {
		try {
			//Create a new dispatcher.
			Dispatcher d = new Dispatcher();
	
			//Create
	        GreetingGiver helloGiver1 = new HelloGiver("HelloGiver1");
	        GreetingGiver helloGiver2 = new HelloGiver("HelloGiver2");
	        
	        //export them to dispatcher
	        RemoteObjectRef ror1 = d.exportRemoteObject("HelloGiver1", GreetingGiver.class.getName(), helloGiver1);
	        RemoteObjectRef ror2 = d.exportRemoteObject("HelloGiver2", GreetingGiver.class.getName(), helloGiver2);
	
			//RemoteObjectRef ror = d.exportRemoteObject("ComputeEngine1", "Compute", ce1);
	        RegistryClient registry = LocateRegistry.getRegistryClient();
	        
			//register them to rmi.
	        registry.bind(ror1);
	        registry.bind(ror2);
	
			d.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
