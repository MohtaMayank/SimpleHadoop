package cmu.cs.distsystems.hw1;

public class TransferDetails {

	private String destHost;
	private int destPort;
	private String processId;
	
	public TransferDetails() {
	}
	
	public TransferDetails(String destHost, int destPort, String processId) {
		super();
		this.destHost = destHost;
		this.destPort = destPort;
		this.processId = processId;
	}
	
	
	public String getDestHost() {
		return destHost;
	}
	public int getDestPort() {
		return destPort;
	}
	public String getProcessId() {
		return processId;
	}
	
	
	
}
