package cmu.cs.distsystems.hw1;

public class Main {

public static void main(String[] args) {
		
		if(args.length < 1 || args.length > 5) {
			printHelp(args);
			System.exit(0);
		}
		
		ProcessManager pm;
		
		boolean isMaster = false;
		String masterHost= null;
		int masterPort = -1;
		int serverPort = 5555;	//DEFAULT 
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("--master")) {
				isMaster = true;
			} else if (args[i].equals("-c")) {
				masterPort = Integer.parseInt(args[i + 1].split(":")[1]);
				masterHost = args[i + 1].split(":")[0];
			} else if (args[i].equals("-port") || args[i].equals("-p")) {
				serverPort = Integer.parseInt(args[i + 1]);
			}
		}
		
		
		if (isMaster) {
			pm = new MasterProcessManager(serverPort);
			pm.start();
        } else {
        	if(masterHost == null ) {
        		printHelp(args);
        	} else {
        		pm = new ProcessManager(masterHost, masterPort, serverPort);
        		pm.start();
        	}
        }
		
		
	}
	
	public static void printHelp(String[] args) {
		System.out.println("Invalid input arguments. Usage:");
		System.out.println(" --master [-p|--port <server-port>]: Indicates that the host is master");
		System.out.println(" OR ");
		System.out.println(" -c <hostname:port> [-p|--port <server-port>]: Indicates that the host is" +
				" a slave and <hostname:port> is where master is running.");
		System.out.println("-p <server-port or --port <server-port> is the port where" +
				" this process manager should run its server to receive messages.");
	}


}
