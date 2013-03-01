package cmu.cs.distsystems.hw2;

import java.rmi.Remote;

public interface GreetingGiver extends Remote {

	public String giveGreeting(String name);

}
