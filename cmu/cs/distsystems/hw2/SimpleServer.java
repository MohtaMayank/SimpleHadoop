package cmu.cs.distsystems.hw2;

/**
 * An example of how a server registers remote objects, registers them 
 * and starts serving requests 
 * @author mayank
 */

public class SimpleServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Create a new dispatcher.
		Dispatcher d = new Dispatcher();
		
		//Make remote objects.
		//Compute ce1 = new ComputeEngine(10);
		//Compute ce2 = new ComputeEngine(20);
		//GreetingGiver helloGiver1 = new HelloGiver();
		
		//export them to dispatcher
		//RemoteObjectRef ror = d.exportRemoteObject("ComputeEngine1", "Compute", ce1);
		
		//register them to rmi.
		
		
		//dipatcher.serve()

	}

}
