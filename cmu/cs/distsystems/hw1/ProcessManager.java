package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



/**
 * The process manager is responsible for starting, managing and load balancing
 * processes across different machines.
 * 
 * @author mayank
 *
 */

public class ProcessManager {
	private static final long HEART_BEAT_INTERVAL = 5000; // 5 seconds
	private static final String PREFIX = "cmu.cs.distsystems.hw1.mp.";
	//Keeps track of all the running processes.
	//TODO: Make this concurrent hash map because different request handlers can
	//simultaneously modify it.
	private Map<String, ProcessHandle> processMap = new ConcurrentHashMap<String, ProcessHandle>();
	
	private String masterHost;
	private int masterPort;
	
	public void setMasterPort(int masterPort) {
		this.masterPort = masterPort;
	}

	private ExecutorService processExecutor;
	
	//TODO: Decide if MasterProcessManager should be a subclass or if
	// we should use composition.
	protected ProcessManager() {
	}
	
	public ProcessManager(String masterAddress) {
		//TODO: what if address not in correct format.
		this.masterHost = masterAddress.split(":")[0];
		this.masterPort = new Integer(masterAddress.split(":")[1]);
	}

    public String getMasterHost(){
        //TODO: return the master host
        return this.masterHost;
    }

    public int getMasterPort(){
        //TODO: return the master port
        return this.masterPort;
    }

    public List<RemoteProcessInfo> getProcessInfoList(){
        List<RemoteProcessInfo> infoList = new ArrayList<RemoteProcessInfo>();

        for(Map.Entry entry:processMap.entrySet()){
            ProcessHandle ph = (ProcessHandle) entry.getValue();
            MigratableProcess mp = ph.getRef();
            infoList.add(mp.getProcessInfo());
        }

        return infoList;
    }

    public HostInformation getHostInformation(){
        return new HostInformation(this.masterHost,this.masterPort,
                this.getProcessInfoList(),new Date());
    }

	
	public void start() {
		
		//TODO: register the process manager with master.
		
		//Instantiate the executor
		processExecutor = Executors.newCachedThreadPool();
		
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
				for(Entry<String, ProcessHandle> entry : processMap.entrySet()) {
					if(entry.getValue().getRef().completed()) {
						synchronized (processMap) {
							processMap.remove(entry.getKey());
						}
					}
				}
				
			} catch (IOException e) {
				//TODO Should we quit or just continue??
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void handleCommand(String command) {
		if(command.equals("ps") ) {
			List<RemoteProcessInfo> l = getProcessInfoList();
			
			for(RemoteProcessInfo rp : l) {
				System.out.println(rp.getCommand());
			}
		} else if (command.equals("quit"))  {
			System.out.println("Quitting ...");
			System.exit(0);
		} else {
			String[] toks = command.split(" ");
			String mpClass = toks[0];
			
			String[] args = Arrays.copyOfRange(toks, 1, toks.length);
			
			Class clazz;
			try {
				clazz = Class.forName(mpClass);
				Constructor ctor = clazz.getConstructor(String[].class);
				Object[] varargs = {args};
				MigratableProcess mp = (MigratableProcess) ctor.newInstance(varargs);
				//Start a new thread.
				Future<?> f = processExecutor.submit(mp);
				
				//TODO: Do we need to sync on Concurrent HashMap?
				synchronized(processMap) {
					processMap.put(mp.getId(), new ProcessHandle(mp, f));
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Invalid Migratable process - No such class");
			}
		}
		
		System.out.print(">>");
	}
	
	public String printCLIHelp() {
		return null;
	}
	
	public Map<String, ProcessHandle> getProcessMap() {
		return processMap;
	}

	public void setProcessMap(Map<String, ProcessHandle> processMap) {
		this.processMap = processMap;
	}

	public void setMasterHost(String masterHost) {
		this.masterHost = masterHost;
	}

	public static void main(String[] args) {
		
		if(args.length < 1 || args.length > 3) {
			printHelp(args);
			System.exit(0);
		}
		
		ProcessManager pm;
		
		if (args[0].equals("--master")) {
			pm = new MasterProcessManager();
			pm.start();
        } else if(args[0].equals("-c")){
			pm = new ProcessManager(args[2]);
        } else{
    		printHelp(args);
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
