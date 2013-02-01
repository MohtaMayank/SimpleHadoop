package cmu.cs.distsystems.hw1;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class HostInformation implements Serializable {

	private static final long serialVersionUID = 9070460687530184794L;

	private String name;
    private int port;
    private List<RemoteProcessInfo> remoteProcesses;
    private Date lastHeartBeat;

    public HostInformation(String name,int port,List<RemoteProcessInfo> infoList,Date timeStamp){
        this.name = name;
        this.port = port;
        this.remoteProcesses = infoList;
        this.lastHeartBeat = timeStamp;
    }
    
    public String getId() {
    	return name + ":" + port;
    }

    public String getName(){
        return this.name;
    }

    public int getPort(){
        return this.port;
    }

    public List<RemoteProcessInfo> getProcessList(){
        return this.remoteProcesses;
    }

    public Date getLastUpdated(){
        return this.lastHeartBeat;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
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
		HostInformation other = (HostInformation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
