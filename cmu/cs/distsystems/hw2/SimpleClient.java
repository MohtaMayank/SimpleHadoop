package cmu.cs.distsystems.hw2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleClient {

    public static void main(String[] args) {
        try{
        	
        	RegistryClient registry = LocateRegistry.getRegistryClient(
        			"localhost", RegistryServer.DEFAULT_REGISTRY_PORT);
        	RemoteObjectRef ror = registry.lookup("HelloGiver1");
        	
        	GreetingGiver g = (GreetingGiver) ror.localise();
        	
        	RemoteObjectRef ror2 = g.locateGreeter("HelloGiver2");
        	
        	GreetingGiver g2 = (GreetingGiver) ror2.localise();
        	
        	System.out.println(g2.collectGreeting("Yuchen", ror));
        	System.out.println(g2.giveGreeting("Mayank", false));
        	
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

}
