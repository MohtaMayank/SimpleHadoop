package cmu.cs.distsystems.hw2;


public class HelloGiver implements GreetingGiver {

	String id;

	public HelloGiver(String id) {
		this.id = id;
	}
	
	@Override
	public String giveGreeting(String name, Boolean throwEx) throws Exception {
		// TODO Auto-generated method stub
		if(throwEx) {
			throw new Exception("You asked for exception ... i will give you one");
		}
		
		return "Hello " + name + " from " + id;
	}

	@Override
	public String collectGreeting(String name, RemoteObjectRef remoteGreeter)
			throws Exception {
		System.out.println(id + " is asking for greeting from " + remoteGreeter.getObjectId());
		GreetingGiver g = (GreetingGiver)remoteGreeter.localise();
		
		return g.giveGreeting(name, false);
	}

	@Override
	public RemoteObjectRef locateGreeter(String greeterName) throws Exception {
		RegistryClient r = LocateRegistry.getRegistryClient();
		System.out.println(id + " is locating greeter " + greeterName);
		return r.lookup("HelloGiver2");
	}

}
