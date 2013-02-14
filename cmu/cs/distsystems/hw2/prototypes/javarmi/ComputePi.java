package cmu.cs.distsystems.hw2.prototypes.javarmi;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ComputePi {
    public static void main(String args[]) {
    	System.setProperty("java.security.policy", 
    			"/home/mayank/workspace/DistributedSystems/src/cmu/cs/distsystems/hw2/prototypes/javarmi/client.policy");
    	System.setProperty("java.rmi.server.codebase", Compute.class.getProtectionDomain().getCodeSource().getLocation().toString());
    	System.setProperty("rmi.server.hostname", "localhost");
    	
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);
            Pi task = new Pi(Integer.parseInt(args[1]));
            BigDecimal pi = comp.executeTask(task);
            System.out.println(pi);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }

}
