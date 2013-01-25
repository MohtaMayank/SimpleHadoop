package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;



/**
 * The process manager is responsible for starting, managing and load balancing
 * processes across different machines.
 * 
 * @author mayank
 *
 */

public class ProcessManager {
	private static final long HEART_BEAT_INTERVAL = 5000; // 5 seconds
	//Keeps track of all the running processes.
	private Map<String, ProcessHandle> processMap = new HashMap<String, ProcessHandle>();
	private boolean isMaster;
	private String masterAddress;
	
	//TODO: Decide if MasterProcessManager should be a subclass or if
	// we should use composition.
	protected ProcessManager() {
		
	}
	
	public ProcessManager(String masterAddress) {
		this.isMaster = false;
		this.masterAddress = masterAddress;
	}
	
	public void start() {
		
		//TODO: register the process manager with master.
		long lastHeartBeatTime = System.currentTimeMillis();
		
		//Setup the command line
		System.out.println();
		System.out.print(">>");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currInput;
		
		//TODO Assumption: This loop does not takes a lot of time - Hence, one input
		//should be processed before user enters new input.
		while(true) {
			try {
				if(br.ready()) {
					currInput = br.readLine();
					handleCommand(currInput);
				}
				
				//TODO: Implement all chores.
				if( (System.currentTimeMillis() - lastHeartBeatTime) > HEART_BEAT_INTERVAL ) {
					//TODO: send heartbeat
				}
				
				//Clear up any completed processes.
				List<String> processesToRemove = new ArrayList<String>();
				for(ProcessHandle ph : processMap.values() ) {
					if(ph.getRef().completed()) {
						ph.getFuture().get();
						processesToRemove.add(ph.getRef().getId());
					}
				}
				for(String id : processesToRemove){
					processMap.remove(id);
				}
				
				//See if any process is suspended?
				
				
			} catch (IOException e) {
				//TODO Should we quit or just continue??
				e.printStackTrace();
			} catch(InterruptedException e ) {
				e.printStackTrace();
			} catch(ExecutionException e)  {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void handleCommand(String command) {
		
	}
	
	public String printCLIHelp() {
		return null;
	}
	
	public static void main(String[] args) {
		
		if(args.length <= 1 || args.length > 3) {
			printHelp(args);
		}
		
		ProcessManager pm;
		
		switch(args[1]) {
			case "--master":
				pm = new MasterProcessManager();
				pm.start();
				break;
			case "-c":
				pm = new ProcessManager(args[2]); 
				break;
			default:
				printHelp(args);
				break;
		}
		
		
	}
	
	public static void printHelp(String[] args) {
		System.out.println("Invalid input arguments. Usage:");
		System.out.println(" --master : Indicates that the host is master");
		System.out.println(" OR ");
		System.out.println(" -c <hostname:port> : Indicates that the host is" +
				" a slave and <hostname:port> is where master is running.");
	}
	
}
