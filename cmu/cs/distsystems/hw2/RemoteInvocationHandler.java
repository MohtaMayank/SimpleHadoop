package cmu.cs.distsystems.hw2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: mimighostipad
 * Date: 2/28/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteInvocationHandler implements InvocationHandler {

    String host;
    int port;
    String objId;

    public RemoteInvocationHandler(String host,int port, String objId){
        this.host = host;
        this.port = port;
        this.objId = objId;
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        InvocationMessage imsg = new InvocationMessage(objId,method.getName(),objects);

        Socket sock = null;

        try{
            sock = new Socket(this.host,this.port);
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(imsg);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
            ReturnMessage rm = (ReturnMessage)ois.readObject();

            if(!rm.isException) return rm.returnVal;
            else{
                throw (Exception) rm.returnVal;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
