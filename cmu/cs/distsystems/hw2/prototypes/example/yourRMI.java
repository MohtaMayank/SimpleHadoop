package cmu.cs.distsystems.hw2.prototypes.example;

/* This does not offer the code of the whole communication module 
   (CM) for RMI: but it gives some hints about how you can make 
   it. I call it simply "yourRMI". 

   For example, it  shows how you can get the host name etc.,
   (well you can hardwire it if you like, I should say),
   or how you can make a class out of classname as a string.

   This just shows one design option. Other options are
   possible. We assume there is a unique skeleton for each
   remote object class (not object) which is called by CM 
   by static methods for unmarshalling etc. We can do without
   it, in which case CM does marshalling/unmarhshalling.
   Which is simpler, I cannot say, since both have their
   own simpleness and complexity.
*/

import java.net.*;

public class yourRMI
{
    static String host;
    static int port;

    // It will use a hash table, which contains ROR together with
    // reference to the remote object.
    // As you can see, the exception handling is not done at all.
    public static void main(String args[])    
	throws Exception
    {
	String InitialClassName = args[0];
	String registryHost = args[1];
	int registryPort = Integer.parseInt(args[2]);	
	String serviceName = args[3];

	// it should have its own port. assume you hardwire it.
	host = (InetAddress.getLocalHost()).getHostName();
	port = 12345;

	// it now have two classes from MainClassName: 
	// (1) the class itself (say ZipCpdeServerImpl) and
	// (2) its skeleton.
	Class initialclass = Class.forName(InitialClassName);
	Class initialskeleton = Class.forName(InitialClassName+"_skel");
	
	// you should also create a remote object table here.
	// it is a table of a ROR and a skeleton.
	// as a hint, I give such a table's interface as RORtbl.java. 
	RORtbl tbl = new RORtbl();
	
	// after that, you create one remote object of initialclass.
	Object o = initialclass.newInstance();
	
	// then register it into the table.
	tbl.addObj(host, port, o);

	// create a socket.
	ServerSocket serverSoc = new ServerSocket(port);

	// Now we go into a loop.
	// Look at rmiregistry.java for a simple server programming.
	// The code is far from optimal but in any way you can get basics.
	// Actually you should use multiple threads, or this easily
	// deadlocks. But for your implementation I do not ask it.
	// For design, consider well.
	while (true)
	    {
		// (1) receives an invocation request.
		// (2) creates a socket and input/output streams.
		// (3) gets the invocation, in martiallled form.
		// (4) gets the real object reference from tbl.
		// (5) Either:
		//      -- using the interface name, asks the skeleton,
		//         together with the object reference, to unmartial
		//         and invoke the real object.
		//      -- or do unmarshalling directly and involkes that
		//         object directly.
		// (6) receives the return value, which (if not marshalled
		//     you should marshal it here) and send it out to the 
		//     the source of the invoker.
		// (7) closes the socket.
	    }
    }
}



