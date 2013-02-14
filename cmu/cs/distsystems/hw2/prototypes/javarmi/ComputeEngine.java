package cmu.cs.distsystems.hw2.prototypes.javarmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class ComputeEngine implements Compute {

    public ComputeEngine() {
        super();
    }

    @Override
    public <T> T executeTask(Task<T> t) {
        return t.execute();
    }

    public static void main(String[] args) {
    	System.setProperty("java.security.policy", 
    			"/home/mayank/workspace/DistributedSystems/src/cmu/cs/distsystems/hw2/prototypes/javarmi/server.policy");
    	System.setProperty("java.rmi.server.codebase", Compute.class.getProtectionDomain().getCodeSource().getLocation().toString());
    	System.setProperty("rmi.server.hostname", "localhost");
    	
    	if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    	
        try {
            String name = "Compute";
            Compute engine = new ComputeEngine();
            Compute stub =
                (Compute) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ComputeEngine bound");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }

}
