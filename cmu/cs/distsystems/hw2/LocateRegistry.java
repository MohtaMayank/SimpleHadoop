package cmu.cs.distsystems.hw2;

/**
 * Simple utility class to create registry client on the server.
 * @author mayank
 */

public class LocateRegistry {

	public static RegistryClient getRegistryClient() {
		//TODO: initialize RegistryClient with local host and default port and return
		return new RegistryClient(RegistryServer.DEFAULT_REGISTRY_PORT, "localhost");
	}
	
	public static RegistryClient getRegistryClient(int port) {
		//TODO: initialize RegistryClient with local host and port and return
		return new RegistryClient(port, "localhost");
	}
	
	public static RegistryClient getRegistryClient(String registryHost, int port) {
		//TODO: initialize RegistryClient with local host and port and return
		return new RegistryClient(port, registryHost);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
