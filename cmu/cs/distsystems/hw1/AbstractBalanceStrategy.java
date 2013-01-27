package cmu.cs.distsystems.hw1;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */



public abstract class AbstractBalanceStrategy {

    protected MasterProcessManager masterPM;

    public AbstractBalanceStrategy(MasterProcessManager masterPM){
        this.masterPM = masterPM;
    }

    public abstract boolean isBalanced();
    //public abstract HostInformation pickLeast();
    //public abstract HostInformation pickMost();
    public abstract TransferChoice processToTransfer();


}
