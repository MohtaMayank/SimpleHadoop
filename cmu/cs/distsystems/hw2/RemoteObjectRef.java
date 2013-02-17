package cmu.cs.distsystems.hw2;

public class RemoteObjectRef
{

	String ipAddr;
    int port;
    String objectId;
    String remoteInterfaceName;

    public RemoteObjectRef(String ip, int port, String obj_key, String riname) {
		this.ipAddr = ip;
		this.port = port;
		this.objectId = obj_key;
		this.remoteInterfaceName = riname;
    }

    // this method is important, since it is a stub creator.
    // 
    Object localise() {
		// Implement this as you like: essentially you should 
		// create a new stub object and returns it.
		// Assume the stub class has the name e.g.
		//
		//       Remote_Interface_Name + "_stub".
		//
		// Then you can create a new stub as follows:
		// 
	    //USE PROXY CODE HERE
		//       Class c = Class.forName(Remote_Interface_Name + "_stub");
		//       Object o = c.newinstance()
	    	
		//
		// For this to work, your stub should have a constructor without arguments.
		// You know what it does when it is called: it gives communication module
		// all what it got (use CM's static methods), including its method name, 
		// arguments etc., in a marshalled form, and CM (yourRMI) sends it out to 
		// another place. 
		// Here let it return null.
		return null;
    }
    
    public String getIpAddr() {
    	return ipAddr;
    }
    
    public int getPort() {
    	return port;
    }
    
    public String getObjectId() {
    	return objectId;
    }
    
    public String getRemoteInterfaceName() {
    	return remoteInterfaceName;
    }
}