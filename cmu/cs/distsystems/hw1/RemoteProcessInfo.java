package cmu.cs.distsystems.hw1;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteProcessInfo {

    private String processId;
    private Date startTime;
    private String command;

    public RemoteProcessInfo(String id,Date startTime,String[] args){
        this.processId = id;
        this.startTime = startTime;
        StringBuilder sb = new StringBuilder();
        for(String arg:args){
            sb.append(arg);
        }
        this.command = sb.toString();
    }


}
