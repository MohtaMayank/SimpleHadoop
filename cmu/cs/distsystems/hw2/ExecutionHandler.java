package cmu.cs.distsystems.hw2;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.Remote;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 2/28/13
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionHandler implements Runnable{

    private InvocationMessage imsg;
    private Remote localObj;
    private Socket client;

    public ExecutionHandler(InvocationMessage imsg, Remote obj, Socket client){
        this.imsg = imsg;
        this.localObj = obj;
        this.client = client;
    }

    @Override
    public void run() {

        String[] types = imsg.types;
        Class[] paraTypes = new Class[types.length];

        for(int i = 0; i < types.length; i++){
            try {
                paraTypes[i] = Class.forName(types[i]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        Method method = null;
        try {
            method = localObj.getClass().getMethod(imsg.methodName,paraTypes);

            Object returnVal = null;
            boolean hasException = false;
            try{
                returnVal = method.invoke(localObj,imsg.args);
            }catch (Exception e){
                hasException = true;
                returnVal = e;
            }

            ReturnMessage rm = new ReturnMessage(returnVal,hasException);

            try{
                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                oos.writeObject(rm);
                oos.flush();
                oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(!client.isClosed()){
                        client.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


    }
}
