package cmu.cs.distsystems.hw3;

/**
 * Launch JVM if required. Send the Map or the Reduce Task to the JVM. 
 *This link might be useful:
 * http://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process/723914#723914
 * Handout says that we should reuse the JVM. In that case, i believe we would need some heartbeat
 * kind of mechanism from the child JVM which would indicate to this thread (running in TaskTracker JVM)
 * about the progress and when it has completed the task and ready to accept new task.
 * Task
 * @author mayank
 */

public class TaskManager implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
