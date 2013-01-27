package cmu.cs.distsystems.hw1;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleBalanceStrategy extends AbstractBalanceStrategy {

    @Override
    public boolean isBalanced(){
        if(masterPM.slaveInfomation.size() < 2){
            return true;
        }

        return false;
    }

    public SimpleBalanceStrategy(MasterProcessManager masterPM){
        super(masterPM);
    }

    @Override
    public TransferChoice processToTransfer(){

        HostInformation leastWork = null;
        HostInformation mostWork = null;

        int maxJob = Integer.MIN_VALUE;
        int minJob = Integer.MAX_VALUE;

        for(Map.Entry entry:masterPM.slaveInfomation.entrySet()){
            HostInformation info = (HostInformation) entry.getValue();
            int workload = info.getProcessList().size();
            if(workload > maxJob) {
                maxJob = workload;
                mostWork = info;
            } else if(workload < minJob){
                minJob = workload;
                leastWork = info;
            }
        }

        if(leastWork != null && mostWork != null){
            String processID = mostWork.getProcessList().get(0).getProcessId();
            return new TransferChoice(mostWork,leastWork,processID);
        }

        return null;
    }

}

