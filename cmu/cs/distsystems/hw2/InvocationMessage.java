package cmu.cs.distsystems.hw2;

import java.io.Serializable;

/**
 * Message send to perform remote method invocation.
 * @author mayank
 */

public class InvocationMessage implements Serializable {

	String objectId;
	String methodName;
	Object[] args;
    String[] types;

    public InvocationMessage(String objectId,String methodName,Object[] args){
        this.objectId = objectId;
        this.methodName = methodName;
        this.args = args;

        String[] types = new String[args.length];
        for(int i = 0; i < types.length; i++){
            types[i] = args[i].getClass().getName();
        }

        this.types = types;
    }

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
