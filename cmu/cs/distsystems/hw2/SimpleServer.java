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
		//Create a new dispatcher.
		Dispatcher d = new Dispatcher();

        Simple s = new Simple();
        d.exportRemoteObject("simple","simpletoo",s);


		
		//Make remote objects.
		//Compute ce1 = new ComputeEngine(10);
		//Compute ce2 = new ComputeEngine(20);
		//GreetingGiver helloGiver1 = new HelloGiver();
		
		//export them to dispatcher
		//RemoteObjectRef ror = d.exportRemoteObject("ComputeEngine1", "Compute", ce1);
		
		//register them to rmi.

		d.serve();

	}

}
