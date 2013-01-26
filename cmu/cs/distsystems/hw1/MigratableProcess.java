package cmu.cs.distsystems.hw1;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Abstract class representing a Migratable process. All processes which have
 * to be migrated have to extend this abstract class.
 * @author mayank
 *
 */
public abstract class MigratableProcess implements Runnable, Serializable {

    private static final long serialVersionUID = -7382360393959001142L;
	
	private volatile boolean suspend = false;
	
	private volatile boolean hasMoreWork = true;
	
	private String[] args;
	private String id;
    private Date startTime;
	
	public MigratableProcess(String args[]) {
		this.args = args;
		this.id = this.getClass().getName() +  "_" + UUID.randomUUID();
        //If unspeicifed new Date() create the current date
        this.startTime = new Date();
	}
	
	@Override
	public void run() {
		this.initialize();

		while(!suspend && hasMoreWork) {
			this.hasMoreWork = this.doNextStep();
		}
	}
	
	public void suspend() {
		suspend = true;
	}

	/**
	 * This method should be able to initialize the 
	 * Migratable process after de-serialization so that
	 * the process can continue where it left off last time it
	 * was suspended.
	 */
	public abstract void initialize();
	
	/**
	 * This method performs the next step in the Migratable process.
	 * The process can be suspended and serialized to be transferred to
	 * any other machine between calls to this method. Hence, before
	 * returning, the method should leave the Migratable process in a 
	 * consistent state so that initialize() function can resume the proccess
	 * from where it left
	 * 
	 * @return : true, if there is more work remaining, false otherwise. 
	 */
	public abstract boolean doNextStep();
	
	/**
	 * Function to check if the MigratableProcess is complete.
	 * @return true if entire work is completed, false otherwise.
	 */
	public boolean completed() {
		return !hasMoreWork;
	}
	
	/**
	 * Returns a unique identifier by which the process can be identified 
	 * @return
	 */
	public String getId() {
		return id;
	}

    public RemoteProcessInfo getProcessInfo(){
        return new RemoteProcessInfo(this.id,this.startTime,this.args);
    }
	
	//TODO: See specification
	public String toString() {
		return args.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigratableProcess other = (MigratableProcess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
