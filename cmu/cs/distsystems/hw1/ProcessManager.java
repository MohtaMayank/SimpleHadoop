package cmu.cs.distsystems.hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;



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
	volatile Map<String, ProcessHandle> processMap = new ConcurrentHashMap<String, ProcessHandle>();
	volatile ExecutorService processExecutor;
	
	String masterHost;
	int masterPort;
	int serverPort;
	
	//Deamon Thread references;
	Thread hbThread;
	Thread pmServerThread;
	
	//TODO: Decide if MasterProcessManager should be a subclass or if
	// we should use composition.
	protected ProcessManager() {
	}
	
	public ProcessManager(String masterHost, int masterPort, int serverPort) {
		//TODO: what if address not in correct format.
		this.masterHost = masterHost;
		this.masterPort = masterPort;
		this.serverPort = serverPort;
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

        for(Entry entry:processMap.entrySet()){
            ProcessHandle ph = (ProcessHandle) entry.getValue();
            MigratableProcess mp = ph.getRef();
            infoList.add(mp.getProcessInfo());
        }

        return infoList;
    }

    public HostInformation getHostInformation(){
		return new HostInformation("localhost" ,this.serverPort,
			        this.getProcessInfoList(),new Date());
    }

	
	public void start() {
		
		//TODO: register the process manager with master.
		
		//Instantiate the executor
		processExecutor = Executors.newCachedThreadPool();
		
		
		//Start HeartBeat Thread - Connects to Master every 5 seconds and sends heartbeat message
		this.hbThread = startHeartBeatService();
		this.hbThread.start();
		
		//Start ProcessManagerServer - different thread
		this.pmServerThread = startPMSlaveServer();
		this.pmServerThread.start();
		
		//Start CLI
		startCLI();
		
	}
	
	public Thread startHeartBeatService() {
		Thread hbThread = new Thread(new HeartBeatThread(this));
		return hbThread;
	}
	
	public Thread startPMSlaveServer() {
		Thread pmServer = new Thread(new ProcessManagerServer(this, serverPort, 
				ProcessManagerRequestHandler.class));
		return pmServer;
	}
	
	public void startCLI() {
		try {
			System.out.println("Slave process manager started on host " + 
					InetAddress.getLocalHost().getHostName() + ":" + serverPort);
			
			//Setup the command line
			System.out.println();
			System.out.print(">>");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String currInput;
			while(true) {
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
				
			}
		} catch (IOException e) {
			//TODO Should we quit or just continue??
			e.printStackTrace();
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
		} else if (command.contains("-transfer")) {
			//THIS IS JUST FOR TESTING TILL MASTER IS READY.
			String[] commandToks = command.split(" ");
			try {
				String fromHostName = commandToks[1];
				int fromHostPort =  Integer.parseInt(commandToks[2]);
				
				String toHostName = commandToks[3];
				int toHostPort =  Integer.parseInt(commandToks[4]);
				
				String pId = commandToks[5];
				
				Socket sock = new Socket(fromHostName, fromHostPort);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				TransferDetails td = new TransferDetails(toHostName, toHostPort, pId);
				oos.writeObject(new Message(Message.TRANSFER_PROCESS_TO, td));
				
				sock.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
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
				System.out.println("Starting new process with Id: " + mp.getId());
				execute(mp);
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
	
	public void execute(MigratableProcess mp) {
		//Start a new thread.
		Future<?> f;
		synchronized (processExecutor) {
			f = processExecutor.submit(mp);
		}
		//TODO: Do we need to sync on Concurrent HashMap?
		synchronized(processMap) {
			processMap.put(mp.getId(), new ProcessHandle(mp, f));
		}
	}
	
	public synchronized void suspend(String processId) {
		
	}

}
