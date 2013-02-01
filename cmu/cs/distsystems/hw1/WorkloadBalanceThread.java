package cmu.cs.distsystems.hw1;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 1/26/13
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkloadBalanceThread implements Runnable{

    private MasterProcessManager masterPM;
    private AbstractBalanceStrategy strategy;
    private int checkInterval = 1000;

    public WorkloadBalanceThread(MasterProcessManager masterPM, AbstractBalanceStrategy strategy){
        this.masterPM = masterPM;
        this.strategy = strategy;
    }



    @Override
    public void run(){

        while(true){

            if(strategy.isBalanced()){
                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                continue;
            }
            
            //TODO: return a list of things to balance
            TransferChoice choice = strategy.processToTransfer();

            if(choice == null) continue;

            HostInformation sourceSlave = choice.getSource();
            HostInformation destSlave = choice.getDestination();

            try {
                Socket messageSender = new Socket(sourceSlave.getName(),sourceSlave.getPort());
                ObjectOutputStream oos = new ObjectOutputStream(messageSender.getOutputStream());
                TransferDetails td = new TransferDetails(destSlave.getName(),
                                            destSlave.getPort(),choice.getProcessID());
                oos.writeObject(new Message(Message.TRANSFER_PROCESS_TO,td));
                oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

}
