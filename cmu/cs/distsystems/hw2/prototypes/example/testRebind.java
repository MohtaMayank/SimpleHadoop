package cmu.cs.distsystems.hw2.prototypes.example;

import java.io.*;

// we test simple registry by binding a service to ROR.

public class testRebind
{

    public static void main(String args[])    
	throws IOException
    {
	// it takes seven arguments.
	// these are it wishes to connect to.
	String host = args[0];
	int port = Integer.parseInt(args[1]);

	// these are data.
	String ServiceName = args[2];
	String IPAdr = args[3];
	int PortNum = Integer.parseInt(args[4]);
	int ObjKey = Integer.parseInt(args[5]);
	String InterfaceName = args[6];

	// make ROR.
	RemoteObjectRef ror = new RemoteObjectRef(IPAdr, PortNum, ObjKey, InterfaceName);

	// this is the ROR content.
	System.out.println("IP address is "+ror.IP_adr);
	System.out.println("Port num is "+ror.Port);
	System.out.println("Object key is "+ror.Obj_Key);
	System.out.println("Interface name is "+ror.Remote_Interface_Name);

	// locate.
	SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);

	System.out.println("located."+sr+"/n");
	
	if (sr != null)
	    {
		// bind.
		sr.rebind(ServiceName, ror);

		// test the binding by looking up.
		RemoteObjectRef ror2 = sr.lookup(ServiceName);

		System.out.println("IP address is "+ror2.IP_adr);
		System.out.println("Port num is "+ror2.Port);
		System.out.println("Object key is "+ror2.Obj_Key);
		System.out.println("Interface name is "+ror2.Remote_Interface_Name);

	    }
	else		
	    {
		System.out.println("no registry found.");
	    }

    }
}














	

	

	
	



	
