package cmu.cs.distsystems.hw1;

import java.net.Socket;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class MasterProcessRequestHandler extends ProcessManagerRequestHandler {

     private MasterProcessManager parentPM;
     private Class<? extends ProcessManagerRequestHandler> handlerType;


    public MasterProcessRequestHandler(ProcessManager pm, Socket clientSocket){
         super(pm,clientSocket);
         this.parentPM = (MasterProcessManager) pm; 
     }

    @Override
    public boolean handleRequest(Message msg){

        if(super.handleRequest(msg)) return true;

        else if(msg.getType().equals(Message.RECIEVE_HEART_BEAT)){
            //TODO: handle the heart beat thing
            recieveHeartBeat(msg);
            return true;
        }
        return false;
    }

    public void recieveHeartBeat(Message msg){
        HostInformation slaveInfo = (HostInformation) msg.objToTransfer;
        
        synchronized (parentPM.slaveInfomation) {
        	this.parentPM.slaveInfomation.put(slaveInfo.getId(), slaveInfo);
		}

        for(Map.Entry entry:parentPM.slaveInfomation.entrySet()){
            Date now = new Date();
            HostInformation info = (HostInformation) entry.getValue();
            if(  now.getSeconds() - info.getLastUpdated().getSeconds() > 6) {
            	synchronized (parentPM.slaveInfomation) {
            		this.parentPM.slaveInfomation.remove((String)entry.getKey());
            	}
            }
        }
    }



}
