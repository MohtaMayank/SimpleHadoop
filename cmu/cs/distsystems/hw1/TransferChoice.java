package cmu.cs.distsystems.hw1;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 9:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferChoice {

    private HostInformation src;
    private HostInformation dest;
    private String processID;

    public TransferChoice(HostInformation src,HostInformation dest,String processID){
        this.src = src;
        this.dest = dest;
        this.processID = processID;
    }

    public HostInformation getSource(){
        return this.src;
    }

    public HostInformation getDestination(){
        return this.dest;
    }

    public String getProcessID(){
        return processID;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result
				+ ((processID == null) ? 0 : processID.hashCode());
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferChoice other = (TransferChoice) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (processID == null) {
			if (other.processID != null)
				return false;
		} else if (!processID.equals(other.processID))
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		return true;
	}

}
