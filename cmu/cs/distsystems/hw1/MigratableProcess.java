package cmu.cs.distsystems.hw1;

import java.io.Serializable;

public interface MigratableProcess extends Runnable, Serializable {

	public String toArgString();
	
	public void suspend();
}
