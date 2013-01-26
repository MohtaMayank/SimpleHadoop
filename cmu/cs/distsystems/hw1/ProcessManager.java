package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
	//TODO: Make this concurrent hash map because different request handlers can
	//simultaneously modify it.
	private Map<String, ProcessHandle> processMap = new ConcurrentHashMap<String, ProcessHandle>();
	private String masterHost;
	private int masterPort;
	
	//TODO: Decide if MasterProcessManager should be a subclass or if
	// we should use composition.
	protected ProcessManager() {
		
	}
	
	public ProcessManager(String masterAddress) {
		//TODO: what if address not in correct format.
		this.masterHost = masterAddress.split(":")[0];
		this.masterPort = new Integer(masterAddress.split(":")[1]);
	}
	
	public void start() {
		
		//TODO: register the process manager with master.
		
		//Setup the command line
		System.out.println();
		System.out.print(">>");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currInput;
		
		//Start HeartBeat Thread - Connects to Master every 5 seconds and sends heartbeat message
		Thread hbThread = new Thread(new HeartBeatThread(this));
		hbThread.start();
		
		//Start ProcessManagerServer
		Thread pmServer = new Thread(new ProcessManagerServer(this));
		pmServer.start();
		
		while(true) {
			try {
				if(br.ready()) {
					currInput = br.readLine();
					handleCommand(currInput);
				}
				//TODO: Check to see if any Migratable process has completed?
				
			} catch (IOException e) {
				//TODO Should we quit or just continue??
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
