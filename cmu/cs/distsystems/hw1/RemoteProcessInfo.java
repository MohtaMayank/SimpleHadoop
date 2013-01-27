package cmu.cs.distsystems.hw1;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteProcessInfo implements Serializable {
	
	private static final long serialVersionUID = 8304666196580654805L;

	private String processId;
    private Date startTime;
    private String command;

    public RemoteProcessInfo(String id, Date startTime, String[] args, String className){
        this.processId = id;
        this.startTime = startTime;
        StringBuilder sb = new StringBuilder();
        sb.append(className + " ");
        for(String arg : args){
            sb.append(arg + " ");
        }
        this.command = sb.toString();
    }
    
    public String getProcessId() {
    	return processId;
    }
    
    public Date getStartTime() {
    	return startTime;
    }
    
    public String getCommand() {
    	return command;
    }

}
