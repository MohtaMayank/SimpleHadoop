package cmu.cs.distsystems.hw1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class MasterProcessManager extends ProcessManager {

    public Map<String, HostInformation> slaveInfomation;

    //public MasterProcessManager()

    public MasterProcessManager(int serverPort){
    	super("localhost", 4444, serverPort);
    	slaveInfomation = new ConcurrentHashMap<String, HostInformation>();
    }

    @Override
    public void start() {
    	
    	try {
			System.out.println("Starting Master PM on " + InetAddress.getLocalHost().getHostAddress()
					+ ":" + 4444);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Start Master Process Manager
    	Thread masterService = new Thread(new ProcessManagerServer
    			( this, 4444, MasterProcessRequestHandler.class) );
    	masterService.start();
    	
    	//Start Work Balancer Server
    	SimpleBalanceStrategy strategy = new SimpleBalanceStrategy(this);
    	Thread loadBalancer = new Thread(new WorkloadBalanceThread(this, strategy));
    	loadBalancer.start();
    	//Start the MigratableProcess executor thread pool.
    	processExecutor = Executors.newCachedThreadPool();
    	
    	//Just sleep for some time to ensure master has started.
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	//Start slave heart beat (Master also runs a slave to which MPs can be submitted)
    	startHeartBeatService();
    	
    	//Start Slave ProcessManagerServer - different thread
		startPMSlaveServer();
		
		
		//Start CLI
		startCLI();
    	
		
    }

}
