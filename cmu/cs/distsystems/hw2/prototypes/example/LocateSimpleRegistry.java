package cmu.cs.distsystems.hw2.prototypes.example;

import java.net.*;
import java.io.*;

public class LocateSimpleRegistry 
{ 
    // this is the SOLE static method.
    // you use it as: LocateSimpleRegistry.getRegistry(123.123.123.123, 2048)
    // and it returns null if there is none, else it returns the registry.
    // actually the registry is just a pair of host IP and port. 
    // inefficient? well you can change it as you like. 
    // for the rest, you can see SimpleRegistry.java.
    public static SimpleRegistry getRegistry(String host, int port)
    {
	// open socket.
	try{
	    Socket soc = new Socket(host, port);
	    
	    // get TCP streams and wrap them. 
	    BufferedReader in = 
		new BufferedReader(new InputStreamReader (soc.getInputStream()));
	    PrintWriter out = 
		new PrintWriter(soc.getOutputStream(), true);
	    
	    // ask.
	    out.println("who are you?");
	    
	    // gets answer.
	    if ((in.readLine()).equals("I am a simple registry."))
		{
		    return new SimpleRegistry(host, port);
		}
	    else
		{
		    System.out.println("somebody is there but not a  registry!");
		    return null;
		}
	}
	catch (Exception e) 
	    { 
		System.out.println("nobody is there!"+e);
		return null;
	    }
    }
}

