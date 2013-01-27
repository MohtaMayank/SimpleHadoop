package cmu.cs.distsystems.hw1;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1282157488263161069L;

	String type;
	Object objToTransfer;
	
	
	public Message(String type, Object objToTransfer) {
		this.type = type;
		this.objToTransfer = objToTransfer;
	}
	
	public String getType() {
		return type;
	}

	public Object getObjToTransfer() {
		return objToTransfer;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
