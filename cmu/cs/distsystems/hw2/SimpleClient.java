package cmu.cs.distsystems.hw2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SimpleClient {

    public static void main(String[] args) throws IOException {

        String[] test = {"me"};

        InvocationMessage imsg = new InvocationMessage("simple","hello",test);

        Socket sock = new Socket("128.237.250.69",5555);

        try{
            ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(imsg);
            oos.flush();
            //oos.close();

            ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
            ReturnMessage rm = (ReturnMessage) ois.readObject();

            System.out.println(rm.returnVal);

            ois.close();
            sock.close();

        }catch (Exception e){

        }
    }

}
