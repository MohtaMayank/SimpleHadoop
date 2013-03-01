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

    public InvocationMessage(String objectId,String methodName,Object[] args){
        this.objectId = objectId;
        this.methodName = methodName;
        this.args = args;
    }

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
