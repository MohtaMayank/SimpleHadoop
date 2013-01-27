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

}
