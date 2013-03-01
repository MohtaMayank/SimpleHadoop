package cmu.cs.distsystems.hw2;

import java.io.Serializable;


public class ReturnMessage implements Serializable {
	private static final long serialVersionUID = -3940254925429376099L;
	
	Object returnVal;
	boolean isException;

	public ReturnMessage(Object returnVal, boolean isException) {
		this.isException = isException;
		this.returnVal = returnVal;
	}
	
	public Object getReturnVal() {
		return returnVal;
	}

	public boolean isException() {
		return isException;
	}
	
}
