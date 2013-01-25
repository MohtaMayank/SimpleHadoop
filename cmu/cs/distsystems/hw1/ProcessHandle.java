package cmu.cs.distsystems.hw1;

import java.util.concurrent.Future;

public class ProcessHandle {
	private MigratableProcess ref;
	private Future<Void> future;
	
	//Shared variable which will be set by the socket thread of ProcessManager if
	//it gets a signal from the master.
	private volatile boolean toSuspend = false;
	
	public ProcessHandle(MigratableProcess ref, Future<Void> future) {
		this.ref = ref;
		this.future = future;
	}

	public MigratableProcess getRef() {
		return ref;
	}
	public Future<Void> getFuture() {
		return future;
	}
	
}
