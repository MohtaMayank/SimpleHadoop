package cmu.cs.distsystems.hw2.prototypes.example;

public class NameServerImpl 
    implements NameServer
{
    String serviceName;
    RemoteObjectRef ro;
    NameServer next;

    public NameServerImpl()
    {
	serviceName="";
	ro=null;
	next=null;
    }

    public NameServerImpl(String s, RemoteObjectRef r, NameServer n)
    {
	serviceName=s;
	ro=r;
	next=n;
    }

    public NameServer add(String s, RemoteObjectRef r, NameServer n)
    {
	return new NameServerImpl(s, r, this);
    }

    public RemoteObjectRef match(String name)
    {
	if (name.equals(serviceName))
	    return ro;
	else
	    return null;
    }
    
    public  NameServer next()
    {
	return next;
    }
}

