package cmu.cs.distsystems.hw2;

import java.net.UnknownHostException;
import java.rmi.Remote;

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
	        GreetingGiver helloGiver = new HelloGiver();
	        
	        //export them to dispatcher
	        RemoteObjectRef ror = d.exportRemoteObject("HelloGiver1", GreetingGiver.class.getName(), helloGiver);
	
			//RemoteObjectRef ror = d.exportRemoteObject("ComputeEngine1", "Compute", ce1);
	        RegistryClient registry = LocateRegistry.getRegistryClient();
	        
			//register them to rmi.
	        registry.bind(ror);
	
			d.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
